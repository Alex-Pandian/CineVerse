import { NavLink, useNavigate } from 'react-router-dom'
import { Film, LogOut, Menu, Search, Star } from 'lucide-react'
import { useState } from 'react'
import { useAuth } from '../context/AuthContext.jsx'
import ThemeToggle from './ThemeToggle.jsx'

const navLinkClass = ({ isActive }) =>
  `flex items-center gap-2 rounded-full px-4 py-2 text-sm font-medium transition ${
    isActive ? 'bg-white/10 text-white' : 'text-slate-300 hover:bg-white/5 hover:text-white'
  }`

const Navbar = () => {
  const { isAuthenticated, logout } = useAuth()
  const [open, setOpen] = useState(false)
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <header className="sticky top-0 z-40 border-b border-white/5 bg-slate-950/90 backdrop-blur">
      <div className="mx-auto flex max-w-7xl items-center justify-between px-4 py-4 sm:px-6 lg:px-10">
        <NavLink to="/" className="flex items-center gap-3 text-lg font-semibold">
          <span className="flex h-10 w-10 items-center justify-center rounded-2xl bg-red-500/90 text-white">
            <Film size={20} />
          </span>
          <span className="font-['Space_Grotesk'] tracking-wide">CineVerse</span>
        </NavLink>

        <nav className="hidden items-center gap-2 md:flex">
          <NavLink to="/" className={navLinkClass}>
            <Search size={16} /> Home
          </NavLink>
          <NavLink to="/trending" className={navLinkClass}>
            <Star size={16} /> Trending
          </NavLink>
          <NavLink to="/favorites" className={navLinkClass}>
            <Film size={16} /> Favorites
          </NavLink>
        </nav>

        <div className="flex items-center gap-3">
          <ThemeToggle />
          {isAuthenticated ? (
            <button
              onClick={handleLogout}
              className="hidden items-center gap-2 rounded-full border border-white/10 px-4 py-2 text-sm font-semibold text-white transition hover:border-red-400 hover:text-red-300 md:flex"
            >
              <LogOut size={16} /> Logout
            </button>
          ) : (
            <NavLink
              to="/login"
              className="hidden rounded-full bg-white px-4 py-2 text-sm font-semibold text-slate-900 transition hover:bg-red-500 hover:text-white md:inline-flex"
            >
              Sign In
            </NavLink>
          )}
          <button
            onClick={() => setOpen((prev) => !prev)}
            className="rounded-full border border-white/10 p-2 text-white md:hidden"
            aria-label="Toggle menu"
          >
            <Menu size={18} />
          </button>
        </div>
      </div>

      {open && (
        <div className="border-t border-white/5 bg-slate-950/95 px-4 py-4 md:hidden">
          <div className="flex flex-col gap-2">
            <NavLink onClick={() => setOpen(false)} to="/" className={navLinkClass}>
              Home
            </NavLink>
            <NavLink onClick={() => setOpen(false)} to="/trending" className={navLinkClass}>
              Trending
            </NavLink>
            <NavLink onClick={() => setOpen(false)} to="/favorites" className={navLinkClass}>
              Favorites
            </NavLink>
            {isAuthenticated ? (
              <button
                onClick={handleLogout}
                className="flex items-center gap-2 rounded-full border border-white/10 px-4 py-2 text-sm font-semibold text-white"
              >
                <LogOut size={16} /> Logout
              </button>
            ) : (
              <NavLink
                onClick={() => setOpen(false)}
                to="/login"
                className="rounded-full bg-white px-4 py-2 text-sm font-semibold text-slate-900"
              >
                Sign In
              </NavLink>
            )}
          </div>
        </div>
      )}
    </header>
  )
}

export default Navbar
