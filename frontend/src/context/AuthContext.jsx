import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import { loginUser, registerUser } from '../services/authService.js'

const AuthContext = createContext(null)

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => localStorage.getItem('token'))
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('user')
    return saved ? JSON.parse(saved) : null
  })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    if (token) {
      localStorage.setItem('token', token)
    } else {
      localStorage.removeItem('token')
    }
  }, [token])

  useEffect(() => {
    if (user) {
      localStorage.setItem('user', JSON.stringify(user))
    } else {
      localStorage.removeItem('user')
    }
  }, [user])

  const login = async (payload) => {
    setLoading(true)
    setError(null)
    try {
      const data = await loginUser(payload)
      console.log(data);
      
      setToken(data.token)
      setUser(data.user || payload)
      return { ok: true }
    } catch (err) {
      setError(err.message || 'Login failed')
      return { ok: false }
    } finally {
      setLoading(false)
    }
  }

  const register = async (payload) => {
    setLoading(true)
    setError(null)
    try {
      const data = await registerUser(payload)
      setToken(data.token)
      setUser(data.user || payload)
      return { ok: true }
    } catch (err) {
      setError(err.message || 'Registration failed')
      return { ok: false }
    } finally {
      setLoading(false)
    }
  }

  const logout = () => {
    setToken(null)
    setUser(null)
  }

  const value = useMemo(
    () => ({ token, user, isAuthenticated: Boolean(token), loading, error, login, register, logout }),
    [token, user, loading, error]
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export const useAuth = () => useContext(AuthContext)
