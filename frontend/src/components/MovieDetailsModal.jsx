import { useEffect, useState } from 'react'
import { X, Heart, Star, Play } from 'lucide-react'
import { fetchMovieDetails } from '../services/movieService.js'
import { clampText, formatRuntime, getImdbId, getPoster } from '../utils/helpers.js'
import { useMovies } from '../context/MoviesContext.jsx'

const fallbackPoster = 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=800&auto=format&fit=crop'

const MovieDetailsModal = ({ imdbId, open, onClose }) => {
  const [movie, setMovie] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const { favorites, addToFavorites, removeFromFavorites, loadFavorites } = useMovies()

  useEffect(() => {
    if (!open || !imdbId) return

    const loadDetails = async () => {
      setLoading(true)
      setError(null)
      try {
        const data = await fetchMovieDetails(imdbId)
        setMovie(data)
      } catch (err) {
        setError(err.message || 'Failed to load movie')
      } finally {
        setLoading(false)
      }
    }

    loadDetails()
  }, [open, imdbId])

  useEffect(() => {
    if (open) {
      loadFavorites()
    }
  }, [open, loadFavorites])

  useEffect(() => {
    const handleKey = (event) => {
      if (event.key === 'Escape') onClose()
    }
    if (open) {
      window.addEventListener('keydown', handleKey)
    }
    return () => window.removeEventListener('keydown', handleKey)
  }, [open, onClose])

  if (!open) return null

  const isFavorite = favorites.some((fav) => getImdbId(fav) && getImdbId(fav) === getImdbId(movie))

  const handleFavorite = () => {
    if (!movie) return
    if (isFavorite) {
      removeFromFavorites(getImdbId(movie))
    } else {
      addToFavorites(movie)
    }
  }

  const handleTrailer = () => {
    const title = movie?.title || movie?.Title
    if (!title) return
    const query = `${title} official trailer`
    const url = `https://www.youtube.com/results?search_query=${encodeURIComponent(query)}`
    window.open(url, '_blank')
  }

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/60 p-4 backdrop-blur-sm opacity-0 animate-[fadeIn_220ms_ease-out_forwards]"
      onClick={onClose}
      role="dialog"
      aria-modal="true"
    >
      <div
        className="relative w-full max-w-4xl overflow-hidden rounded-3xl border border-white/10 bg-slate-950/95 shadow-2xl opacity-0 translate-y-4 scale-95 animate-[modalIn_240ms_ease-out_forwards]"
        onClick={(event) => event.stopPropagation()}
      >
        <button
          onClick={onClose}
          className="absolute right-4 top-4 rounded-full border border-white/10 bg-white/5 p-2 text-slate-200 transition hover:border-white/20 hover:bg-white/10"
          aria-label="Close"
        >
          <X size={16} />
        </button>

        {loading && (
          <div className="grid gap-6 p-8 lg:grid-cols-[1fr_2fr]">
            <div className="h-72 animate-pulse rounded-2xl bg-white/5" />
            <div className="space-y-4">
              <div className="h-6 w-2/3 animate-pulse rounded bg-white/10" />
              <div className="h-4 w-1/2 animate-pulse rounded bg-white/10" />
              <div className="h-4 w-full animate-pulse rounded bg-white/10" />
            </div>
          </div>
        )}

        {error && (
          <div className="p-8 text-sm text-red-200">{error}</div>
        )}

        {!loading && !error && movie && (
          <div className="grid gap-8 p-8 lg:grid-cols-[1fr_2fr]">
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
                <h2 className="text-3xl font-semibold text-white md:text-4xl">{movie.title || movie.Title}</h2>
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
              <div className="flex flex-wrap items-center gap-3">
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
                <button
                  onClick={handleTrailer}
                  disabled={!movie?.title && !movie?.Title}
                  className="inline-flex items-center gap-2 rounded-full border border-white/10 px-5 py-3 text-sm font-semibold text-slate-200 transition hover:border-white/20 hover:bg-white/10 disabled:cursor-not-allowed disabled:opacity-40"
                >
                  <Play size={16} /> Watch Trailer
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

export default MovieDetailsModal
