import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { ArrowLeft, Heart, Star } from 'lucide-react'
import Layout from '../components/Layout.jsx'
import SkeletonCard from '../components/SkeletonCard.jsx'
import ErrorState from '../components/ErrorState.jsx'
import { fetchMovieDetails } from '../services/movieService.js'
import { clampText, formatRuntime, getImdbId, getPoster } from '../utils/helpers.js'
import { useMovies } from '../context/MoviesContext.jsx'

const fallbackPoster = 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=800&auto=format&fit=crop'

const MovieDetailsPage = () => {
  const { id } = useParams()
  const [movie, setMovie] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const { favorites, addToFavorites, removeFromFavorites, loadFavorites } = useMovies()

  useEffect(() => {
    const loadDetails = async () => {
      setLoading(true)
      setError(null)
      try {
        const data = await fetchMovieDetails(id)
        //console.log(data);
        
        setMovie(data)
      } catch (err) {
        setError(err.message || 'Failed to load movie')
      } finally {
        setLoading(false)
      }
    }

    loadDetails()
  }, [id])

  useEffect(() => {
    loadFavorites()
  }, [loadFavorites])

  const isFavorite = favorites.some((fav) => getImdbId(fav) && getImdbId(fav) === getImdbId(movie))

  const handleFavorite = () => {
    if (!movie) return
    if (isFavorite) {
      removeFromFavorites(getImdbId(movie))
    } else {
      addToFavorites(movie)
    }
  }

  return (
    <Layout>
      <div className="mb-6 flex items-center gap-3">
        <Link
          to="/"
          className="inline-flex items-center gap-2 rounded-full border border-white/10 px-4 py-2 text-xs font-semibold uppercase tracking-widest text-slate-300"
        >
          <ArrowLeft size={14} /> Back
        </Link>
      </div>

      {loading && (
        <div className="grid gap-6 lg:grid-cols-[1fr_2fr]">
          <SkeletonCard />
          <div className="space-y-4">
            <div className="h-6 w-2/3 animate-pulse rounded bg-slate-800/60" />
            <div className="h-4 w-1/2 animate-pulse rounded bg-slate-800/60" />
            <div className="h-4 w-full animate-pulse rounded bg-slate-800/60" />
          </div>
        </div>
      )}

      {error && <ErrorState message={error} />}

      {!loading && !error && movie && (
        <div className="grid gap-8 lg:grid-cols-[1fr_2fr]">
          <div className="overflow-hidden rounded-3xl border border-white/10 bg-white/5">
            <img
              src={getPoster(movie.poster || movie.Poster)}
              alt={movie.title || movie.Title}
              onError={(event) => {
                if (event.currentTarget.src !== fallbackPoster) {
                  event.currentTarget.src = fallbackPoster
                }
              }}
              className="h-full w-full object-cover"
            />
          </div>
          <div className="space-y-5">
            <div className="flex flex-wrap items-center gap-3">
              <h1 className="text-3xl font-semibold text-white md:text-4xl">{movie.title || movie.Title}</h1>
              <span className="rounded-full bg-white/10 px-3 py-1 text-xs text-slate-300">
                {movie.year || movie.Year}
              </span>
            </div>
            <div className="flex flex-wrap items-center gap-4 text-sm text-slate-300">
              <span className="inline-flex items-center gap-2">
                <Star size={16} className="text-yellow-300" /> {movie.imdbRating || 'N/A'} IMDb
              </span>
              <span>{formatRuntime(movie.runtime || movie.Runtime)}</span>
              <span>{movie.genre || movie.Genre || '—'}</span>
            </div>
            <p className="text-sm text-slate-400">{clampText(movie.plot || movie.Plot, 260)}</p>
            <div className="grid gap-3 text-sm text-slate-300">
              <p>
                <span className="text-slate-500">Actors:</span> {movie.actors || movie.Actors || 'N/A'}
              </p>
              <p>
                <span className="text-slate-500">Genre:</span> {movie.genre || movie.Genre || 'N/A'}
              </p>
            </div>
            <button
              onClick={handleFavorite}
              className={`inline-flex items-center gap-2 rounded-full px-5 py-3 text-sm font-semibold transition ${
                isFavorite
                  ? 'bg-red-500/20 text-red-200'
                  : 'bg-white text-slate-900 hover:bg-red-500 hover:text-white'
              }`}
            >
              <Heart size={16} /> {isFavorite ? 'Remove from Favorites' : 'Add to Favorites'}
            </button>
          </div>
        </div>
      )}
    </Layout>
  )
}

export default MovieDetailsPage
