import api from './api.js'

export const searchMovies = async (name, page = 1) => {
  const { data } = await api.get(`/movies?name=${encodeURIComponent(name)}&page=${page}`)
  return data
}

export const fetchMovieDetails = async (id) => {
  const { data } = await api.get(`/movies/${id}`)
  return data
}

export const fetchFavorites = async () => {
  const { data } = await api.get('/favorites')
  return data
}

export const addFavoriteMovie = async (movie) => {
  const imdbId = movie?.imdbId || movie?.imdbID || movie?.id
  const title = movie?.title || movie?.Title
  const poster = movie?.poster || movie?.Poster

  if (!imdbId) {
    throw new Error('Movie IMDb ID is required to add favorites')
  }
  if (!title) {
    throw new Error('Movie title is required to add favorites')
  }

  const { data } = await api.post(`/favorites/${imdbId}`, null, {
    params: { title, poster },
  })
  return data
}

export const removeFavoriteMovie = async (id) => {
  const { data } = await api.delete(`/favorites/${id}`)
  return data
}

export const fetchTrending = async () => {
  const { data } = await api.get('/trending')
  return data
}

export const fetchRecommendations = async () => {
  const { data } = await api.get('/recommendations')
  return data
}
