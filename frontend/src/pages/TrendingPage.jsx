import { useEffect, useState } from 'react'
import Layout from '../components/Layout.jsx'
import SectionHeader from '../components/SectionHeader.jsx'
import MovieGrid from '../components/MovieGrid.jsx'
import MovieDetailsModal from '../components/MovieDetailsModal.jsx'
import SkeletonCard from '../components/SkeletonCard.jsx'
import EmptyState from '../components/EmptyState.jsx'
import { useMovies } from '../context/MoviesContext.jsx'
import { getImdbId } from '../utils/helpers.js'

const TrendingPage = () => {
  const { trending, loadTrending, loading, favorites, addToFavorites, removeFromFavorites } = useMovies()
  const [activeMovieId, setActiveMovieId] = useState(null)

  useEffect(() => {
    loadTrending()
  }, [loadTrending])

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
      <SectionHeader
        title="Trending Now"
        subtitle="The most searched titles right now, updated in real time."
      />
      <div className="mt-6">
        {loading.trending ? (
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
            {Array.from({ length: 8 }).map((_, index) => (
              <SkeletonCard key={index} />
            ))}
          </div>
        ) : trending.length > 0 ? (
          <MovieGrid
            movies={trending}
            onFavorite={handleFavorite}
            favorites={favorites}
            onOpen={handleOpenDetails}
          />
        ) : (
          <EmptyState
            title="No trending titles"
            description="Trending data will show up once the backend begins tracking searches."
          />
        )}
      </div>
      <MovieDetailsModal
        open={Boolean(activeMovieId)}
        imdbId={activeMovieId}
        onClose={() => setActiveMovieId(null)}
      />
    </Layout>
  )
}

export default TrendingPage
