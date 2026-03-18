import { useEffect, useState } from 'react'
import Layout from '../components/Layout.jsx'
import SectionHeader from '../components/SectionHeader.jsx'
import MovieGrid from '../components/MovieGrid.jsx'
import MovieDetailsModal from '../components/MovieDetailsModal.jsx'
import EmptyState from '../components/EmptyState.jsx'
import SkeletonCard from '../components/SkeletonCard.jsx'
import { useMovies } from '../context/MoviesContext.jsx'
import { getImdbId } from '../utils/helpers.js'

const FavoritesPage = () => {
  const { favorites, loadFavorites, loading, removeFromFavorites } = useMovies()
  const [activeMovieId, setActiveMovieId] = useState(null)

  useEffect(() => {
    loadFavorites()
  }, [loadFavorites])

  const handleFavorite = (movie) => {
    removeFromFavorites(getImdbId(movie))
  }

  const handleOpenDetails = (imdbId) => {
    setActiveMovieId(imdbId)
  }

  return (
    <Layout>
      <SectionHeader
        title="Your Favorites"
        subtitle="All the movies and series you have saved for later."
      />
      <div className="mt-6">
        {loading.favorites ? (
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
            {Array.from({ length: 6 }).map((_, index) => (
              <SkeletonCard key={index} />
            ))}
          </div>
        ) : favorites.length > 0 ? (
          <MovieGrid
            movies={favorites}
            onFavorite={handleFavorite}
            favorites={favorites}
            onOpen={handleOpenDetails}
          />
        ) : (
          <EmptyState
            title="No favorites yet"
            description="Tap the heart on any movie to add it to your favorites list."
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

export default FavoritesPage
