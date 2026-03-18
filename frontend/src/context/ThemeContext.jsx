import { createContext, useContext, useEffect } from 'react'
import useLocalStorage from '../hooks/useLocalStorage.js'

const ThemeContext = createContext(null)

export const ThemeProvider = ({ children }) => {
  const [theme, setTheme] = useLocalStorage('theme', 'dark')

  useEffect(() => {
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
    const nextTheme = theme || (prefersDark ? 'dark' : 'light')
    document.documentElement.classList.toggle('dark', nextTheme === 'dark')
    document.documentElement.dataset.theme = nextTheme
  }, [theme])

  const toggleTheme = () => {
    setTheme((prev) => (prev === 'dark' ? 'light' : 'dark'))
  }

  return <ThemeContext.Provider value={{ theme, toggleTheme }}>{children}</ThemeContext.Provider>
}

export const useTheme = () => useContext(ThemeContext)
