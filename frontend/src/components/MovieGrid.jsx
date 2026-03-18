import MovieCard from './MovieCard.jsx'
import { getImdbId } from '../utils/helpers.js'

const MovieGrid = ({ movies = [], onFavorite, favorites = [], onOpen }) => {
  return (
    <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
      {movies.map((movie) => (
        <MovieCard
          key={getImdbId(movie) || movie.Title}
          movie={movie}
          onFavorite={onFavorite}
          onOpen={onOpen}
          isFavorite={favorites.some((fav) => getImdbId(fav) && getImdbId(fav) === getImdbId(movie))}
        />
      ))}
    </div>
  )
}

export default MovieGrid
