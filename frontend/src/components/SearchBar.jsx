import { useRef, useState } from 'react'
import { Search } from 'lucide-react'
import useOutsideClick from '../hooks/useOutsideClick.js'

const SearchBar = ({ value, onChange, onSubmit, suggestions = [], history = [], onSelect }) => {
  const [open, setOpen] = useState(false)
  const containerRef = useRef(null)

  useOutsideClick(containerRef, () => setOpen(false))

  const handleSelect = (item) => {
    onSelect(item)
    setOpen(false)
  }

  return (
    <div ref={containerRef} className="relative">
      <form
        onSubmit={(event) => {
          event.preventDefault()
          onSubmit()
        }}
        className="flex items-center gap-3 rounded-3xl border border-white/10 bg-white/5 px-4 py-3 shadow-lg shadow-black/20"
      >
        <Search size={18} className="text-slate-300" />
        <input
          value={value}
          onChange={(event) => {
            onChange(event.target.value)
            setOpen(true)
          }}
          onFocus={() => setOpen(true)}
          type="text"
          placeholder="Search movies, series, actors..."
          className="w-full bg-transparent text-sm text-white outline-none placeholder:text-slate-400"
        />
        <button
          type="submit"
          className="rounded-full bg-red-500 px-4 py-2 text-xs font-semibold text-white transition hover:bg-red-400"
        >
          Search
        </button>
      </form>

      {open && (suggestions.length > 0 || history.length > 0) && (
        <div className="absolute left-0 right-0 top-full z-20 mt-3 overflow-hidden rounded-2xl border border-white/10 bg-slate-950/95 shadow-xl backdrop-blur">
          {suggestions.length > 0 && (
            <div className="border-b border-white/5 px-4 py-3">
              <p className="text-xs font-semibold uppercase tracking-widest text-slate-500">Suggestions</p>
              <div className="mt-2 flex flex-col gap-2">
                {suggestions.map((item) => (
                  <button
                    key={item.imdbId || item.id || item.Title}
                    onClick={() => handleSelect(item.title || item.Title || item.name)}
                    className="text-left text-sm text-slate-200 transition hover:text-white"
                  >
                    {item.title || item.Title || item.name}
                  </button>
                ))}
              </div>
            </div>
          )}
          {history.length > 0 && (
            <div className="px-4 py-3">
              <p className="text-xs font-semibold uppercase tracking-widest text-slate-500">Recent Searches</p>
              <div className="mt-2 flex flex-wrap gap-2">
                {history.map((term) => (
                  <button
                    key={term}
                    onClick={() => handleSelect(term)}
                    className="rounded-full border border-white/10 px-3 py-1 text-xs text-slate-300 transition hover:border-white/30 hover:text-white"
                  >
                    {term}
                  </button>
                ))}
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  )
}

export default SearchBar
