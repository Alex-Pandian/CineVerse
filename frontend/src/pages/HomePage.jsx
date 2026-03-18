import { useEffect, useMemo, useState } from 'react'
import { Play, Sparkles } from 'lucide-react'
import Layout from '../components/Layout.jsx'
import SearchBar from '../components/SearchBar.jsx'
import FiltersBar from '../components/FiltersBar.jsx'
import MovieGrid from '../components/MovieGrid.jsx'
import SkeletonCard from '../components/SkeletonCard.jsx'
import EmptyState from '../components/EmptyState.jsx'
import SectionHeader from '../components/SectionHeader.jsx'
import MovieDetailsModal from '../components/MovieDetailsModal.jsx'
import ErrorState from '../components/ErrorState.jsx'
import useDebounce from '../hooks/useDebounce.js'
import { useMovies } from '../context/MoviesContext.jsx'
import { searchMovies } from '../services/movieService.js'
import { getImdbId } from '../utils/helpers.js'

const HomePage = () => {
  const [query, setQuery] = useState('')
  const [year, setYear] = useState('')
  const [type, setType] = useState('all')
  const [results, setResults] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [page, setPage] = useState(1)
  const [hasNext, setHasNext] = useState(false)
  const [totalPages, setTotalPages] = useState(0)
  const [activeMovieId, setActiveMovieId] = useState(null)
  const debouncedQuery = useDebounce(query, 450)

  const {
    favorites,
    addToFavorites,
    removeFromFavorites,
    searchHistory,
    addSearch,
    recommendations,
    loadRecommendations,
    loading: movieLoading,
  } = useMovies()

  useEffect(() => {
    loadRecommendations()
  }, [loadRecommendations])

  useEffect(() => {
    const fetchResults = async () => {
      if (!debouncedQuery) {
        setResults([])
        setHasNext(false)
        setTotalPages(0)
        setPage(1)
        return
      }

      setLoading(true)
      setError(null)
      try {
        const data = await searchMovies(debouncedQuery, page)
        const nextResults = Array.isArray(data) ? data : data?.results || []
        const nextTotalPages = data?.totalPages || 0
        setResults(nextResults)
        setTotalPages(nextTotalPages)
        setHasNext(nextTotalPages ? page < nextTotalPages : nextResults.length === 10)
      } catch (err) {
        setError(err.message || 'Failed to search movies')
        setHasNext(false)
        setTotalPages(0)
      } finally {
        setLoading(false)
      }
    }

    fetchResults()
  }, [debouncedQuery, page])

  useEffect(() => {
    if (debouncedQuery) {
      setPage(1)
    }
  }, [debouncedQuery])

  const filteredResults = useMemo(() => {
    return results.filter((movie) => {
      const matchesYear = year ? String(movie.year || movie.Year) === String(year) : true
      const matchesType = type === 'all' ? true : (movie.type || movie.Type) === type
      return matchesYear && matchesType
    })
  }, [results, year, type])

  const suggestions = useMemo(() => results.slice(0, 5), [results])

  const handleSubmit = () => {
    if (query.trim()) {
      addSearch(query.trim())
      setPage(1)
    }
  }

  const handleSelect = (term) => {
    setQuery(term)
    addSearch(term)
    setPage(1)
  }

  const handleFavorite = (movie) => {
    const isFav = favorites.some((fav) => getImdbId(fav) && getImdbId(fav) === getImdbId(movie))
    if (isFav) {
      removeFromFavorites(getImdbId(movie))
    } else {
      addToFavorites(movie)
    }
  }

  const handleOpenDetails = (imdbId) => {
    setActiveMovieId(imdbId)
  }

  return (
    <Layout>
      <section className="relative overflow-hidden rounded-3xl border border-white/10 bg-gradient-to-br from-slate-950 via-slate-900 to-slate-950 p-8">
        <div className="absolute inset-0 opacity-40">
          <div className="absolute -left-20 -top-10 h-64 w-64 rounded-full bg-red-500/20 blur-3xl" />
          <div className="absolute bottom-0 right-0 h-72 w-72 rounded-full bg-indigo-500/20 blur-3xl" />
        </div>
        <div className="relative z-10 flex flex-col gap-6">
          <div className="flex flex-col gap-3">
            <p className="flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.4em] text-red-400">
              <Sparkles size={14} /> Curated Movie Discovery
            </p>
            <h1 className="max-w-2xl text-3xl font-semibold text-white md:text-5xl">
              Search, track, and build your personal watchlist across every screen.
            </h1>
            <p className="max-w-2xl text-sm text-slate-400 md:text-base">
              Dive into trending picks, personalized recommendations, and cinematic gems. Your next movie night
              starts here.
            </p>
          </div>
          <div className="flex flex-col gap-4">
            <SearchBar
              value={query}
              onChange={setQuery}
              onSubmit={handleSubmit}
              suggestions={suggestions}
              history={searchHistory}
              onSelect={handleSelect}
            />
            <FiltersBar year={year} onYearChange={setYear} type={type} onTypeChange={setType} />
          </div>
        </div>
      </section>

      <section className="mt-10">
        <SectionHeader
          title="Search Results"
          subtitle="Start typing to explore movies, series, and cult classics in real time."
          action={
            <button className="inline-flex items-center gap-2 rounded-full border border-white/10 px-4 py-2 text-xs font-semibold uppercase tracking-widest text-slate-300">
              <Play size={14} /> Live Results
            </button>
          }
        />

        <div className="mt-6">
          {loading && (
            <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
              {Array.from({ length: 8 }).map((_, index) => (
                <SkeletonCard key={index} />
              ))}
            </div>
          )}

          {error && <ErrorState message={error} />}

          {!loading && !error && filteredResults.length > 0 && (
            <>
              <MovieGrid
                movies={filteredResults}
                onFavorite={handleFavorite}
                favorites={favorites}
                onOpen={handleOpenDetails}
              />
              <div className="mt-8 flex flex-wrap items-center justify-center gap-4">
                <button
                  type="button"
                  onClick={() => setPage((prev) => Math.max(1, prev - 1))}
                  disabled={page === 1}
                  className="rounded-full border border-white/10 px-4 py-2 text-xs font-semibold uppercase tracking-widest text-slate-300 transition hover:border-white/30 hover:text-white disabled:cursor-not-allowed disabled:opacity-40"
                >
                  Previous
                </button>
                {totalPages > 0 && (
                  <div className="flex flex-wrap items-center justify-center gap-2">
                    {Array.from({ length: totalPages }, (_, index) => {
                      const pageNumber = index + 1
                      const isActive = pageNumber === page
                      return (
                        <button
                          key={pageNumber}
                          type="button"
                          onClick={() => setPage(pageNumber)}
                          className={`rounded-full border px-3 py-2 text-xs font-semibold uppercase tracking-widest transition ${
                            isActive
                              ? 'border-red-500 bg-red-500/20 text-red-200'
                              : 'border-white/10 text-slate-300 hover:border-white/30 hover:text-white'
                          }`}
                        >
                          {pageNumber}
                        </button>
                      )
                    })}
                  </div>
                )}
                <button
                  type="button"
                  onClick={() => setPage((prev) => prev + 1)}
                  disabled={!hasNext}
                  className="rounded-full border border-white/10 px-4 py-2 text-xs font-semibold uppercase tracking-widest text-slate-300 transition hover:border-white/30 hover:text-white disabled:cursor-not-allowed disabled:opacity-40"
                >
                  Next
                </button>
              </div>
            </>
          )}

          {!loading && !error && filteredResults.length === 0 && (
            <EmptyState
              title="No movies yet"
              description="Search for a movie name to start exploring. We'll show suggestions as you type."
            />
          )}
        </div>
      </section>

      <section className="mt-14">
        <SectionHeader
          title="Recommended For You"
          subtitle="Based on your recent searches and watchlist activity."
        />
        <div className="mt-6">
          {movieLoading.recs ? (
            <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
              {Array.from({ length: 4 }).map((_, index) => (
                <SkeletonCard key={index} />
              ))}
            </div>
          ) : recommendations.length > 0 ? (
            <MovieGrid
              movies={recommendations}
              onFavorite={handleFavorite}
              favorites={favorites}
              onOpen={handleOpenDetails}
            />
          ) : (
            <EmptyState
              title="No recommendations yet"
              description="Keep searching and adding favorites to unlock personalized picks."
            />
          )}
        </div>
      </section>
      <MovieDetailsModal
        open={Boolean(activeMovieId)}
        imdbId={activeMovieId}
        onClose={() => setActiveMovieId(null)}
      />
    </Layout>
  )
}

export default HomePage
