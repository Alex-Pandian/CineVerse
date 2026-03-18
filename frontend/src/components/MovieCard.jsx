import { Heart } from 'lucide-react'
import { getImdbId, getPoster } from '../utils/helpers.js'

const fallbackPoster = 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=800&auto=format&fit=crop'

const MovieCard = ({ movie, onFavorite, isFavorite, onOpen }) => {
  const posterSrc = getPoster(movie.poster || movie.Poster)
  const imdbId = getImdbId(movie)

  return (
    <div className="group relative overflow-hidden rounded-3xl border border-white/10 bg-white/5 shadow-lg shadow-black/20 transition hover:-translate-y-2 hover:border-white/20">
      <button
        type="button"
        onClick={() => onOpen?.(imdbId)}
        className="block w-full text-left"
        aria-label={`Open details for ${movie.title || movie.Title}`}
      >
        <div className="relative h-72 overflow-hidden">
          <img
            src={posterSrc}
            alt={movie.title || movie.Title}
            onError={(event) => {
              if (event.currentTarget.src !== fallbackPoster) {
                event.currentTarget.src = fallbackPoster
              }
            }}
            className="h-full w-full object-cover transition duration-500 group-hover:scale-105"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-slate-950 via-transparent" />
        </div>
      </button>
      <div className="flex items-start justify-between gap-3 px-4 py-4">
        <div>
          <h3 className="text-base font-semibold text-white">
            {movie.title || movie.Title}
          </h3>
          <p className="text-xs uppercase tracking-widest text-slate-400">
            {movie.year || movie.Year || '—'}
          </p>
        </div>
        {onFavorite && (
          <button
            onClick={() => onFavorite(movie)}
            className={`rounded-full border p-2 transition ${
              isFavorite
                ? 'border-red-500 bg-red-500/20 text-red-300'
                : 'border-white/10 text-slate-300 hover:border-white/30 hover:text-white'
            }`}
            aria-label="Toggle favorite"
          >
            <Heart size={16} />
          </button>
        )}
      </div>
    </div>
  )
}

export default MovieCard
