import { createContext, useCallback, useContext, useMemo, useState } from 'react'
import useLocalStorage from '../hooks/useLocalStorage.js'
import {
  addFavoriteMovie,
  fetchFavorites,
  fetchRecommendations,
  fetchTrending,
  removeFavoriteMovie,
} from '../services/movieService.js'

const MoviesContext = createContext(null)

export const MoviesProvider = ({ children }) => {
  const [favorites, setFavorites] = useState([])
  const [trending, setTrending] = useState([])
  const [recommendations, setRecommendations] = useState([])
  const [loading, setLoading] = useState({ favorites: false, trending: false, recs: false })
  const [error, setError] = useState(null)
  const [searchHistory, setSearchHistory] = useLocalStorage('searchHistory', [])

  const loadFavorites = useCallback(async () => {
    setLoading((prev) => ({ ...prev, favorites: true }))
    setError(null)
    try {
      const data = await fetchFavorites()
      setFavorites(data || [])
    } catch (err) {
      setError(err.message || 'Failed to load favorites')
    } finally {
      setLoading((prev) => ({ ...prev, favorites: false }))
    }
  }, [])

  const loadTrending = useCallback(async () => {
    setLoading((prev) => ({ ...prev, trending: true }))
    setError(null)
    try {
      const data = await fetchTrending()
      setTrending(data || [])
    } catch (err) {
      setError(err.message || 'Failed to load trending')
    } finally {
      setLoading((prev) => ({ ...prev, trending: false }))
    }
  }, [])

  const loadRecommendations = useCallback(async () => {
    setLoading((prev) => ({ ...prev, recs: true }))
    setError(null)
    try {
      const data = await fetchRecommendations()
      setRecommendations(data || [])
    } catch (err) {
      setError(err.message || 'Failed to load recommendations')
    } finally {
      setLoading((prev) => ({ ...prev, recs: false }))
    }
  }, [])

  const addToFavorites = useCallback(
    async (movie) => {
      await addFavoriteMovie(movie)
      await loadFavorites()
    },
    [loadFavorites]
  )

  const removeFromFavorites = useCallback(
    async (id) => {
      await removeFavoriteMovie(id)
      await loadFavorites()
    },
    [loadFavorites]
  )

  const addSearch = (term) => {
    if (!term) return
    setSearchHistory((prev) => {
      const next = [term, ...prev.filter((item) => item !== term)].slice(0, 7)
      return next
    })
  }

  const value = useMemo(
    () => ({
      favorites,
      trending,
      recommendations,
      loading,
      error,
      loadFavorites,
      loadTrending,
      loadRecommendations,
      addToFavorites,
      removeFromFavorites,
      searchHistory,
      addSearch,
    }),
    [
      favorites,
      trending,
      recommendations,
      loading,
      error,
      loadFavorites,
      loadTrending,
      loadRecommendations,
      addToFavorites,
      removeFromFavorites,
      searchHistory,
    ]
  )

  return <MoviesContext.Provider value={value}>{children}</MoviesContext.Provider>
}

export const useMovies = () => useContext(MoviesContext)
