import { MOVIE_TYPES } from '../utils/constants.js'

const FiltersBar = ({ year, onYearChange, type, onTypeChange }) => {
  return (
    <div className="flex flex-wrap gap-3 rounded-3xl border border-white/10 bg-white/5 px-4 py-3">
      <input
        type="number"
        value={year}
        onChange={(event) => onYearChange(event.target.value)}
        placeholder="Year"
        min="1900"
        max={new Date().getFullYear()}
        className="w-28 rounded-full border border-white/10 bg-transparent px-3 py-2 text-sm text-white outline-none placeholder:text-slate-500"
      />
      <div className="flex flex-wrap gap-2">
        {MOVIE_TYPES.map((item) => (
          <button
            key={item.value}
            onClick={() => onTypeChange(item.value)}
            className={`rounded-full px-4 py-2 text-xs font-semibold uppercase tracking-widest transition ${
              type === item.value
                ? 'bg-white text-slate-900'
                : 'border border-white/10 text-slate-300 hover:border-white/30 hover:text-white'
            }`}
          >
            {item.label}
          </button>
        ))}
      </div>
    </div>
  )
}

export default FiltersBar
