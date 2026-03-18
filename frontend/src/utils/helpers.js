export const formatRuntime = (runtime) => {
  if (!runtime) return 'N/A'
  return runtime.includes('min') ? runtime : `${runtime} min`
}

export const clampText = (text, max = 160) => {
  if (!text) return 'No description available.'
  if (text.length <= max) return text
  return `${text.slice(0, max).trim()}...`
}

export const getPoster = (poster) => {
  if (!poster || poster === 'N/A') {
    return 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=800&auto=format&fit=crop'
  }
  return poster
}

export const getImdbId = (movie) => {
  return movie?.imdbId || movie?.imdbID || movie?.id || movie?.imdb_id || null
}
