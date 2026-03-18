const AuthForm = ({ title, subtitle, fields, submitLabel, onSubmit, loading, error, footer }) => {
  return (
    <div className="w-full max-w-md rounded-3xl border border-white/10 bg-slate-950/80 p-8 shadow-2xl">
      <div className="space-y-2">
        <h1 className="text-2xl font-semibold text-white">{title}</h1>
        <p className="text-sm text-slate-400">{subtitle}</p>
      </div>
      <form
        onSubmit={onSubmit}
        className="mt-6 space-y-4"
      >
        {fields}
        {error && <p className="rounded-2xl border border-red-500/30 bg-red-500/10 px-4 py-3 text-xs text-red-200">{error}</p>}
        <button
          type="submit"
          disabled={loading}
          className="w-full rounded-full bg-white px-4 py-3 text-sm font-semibold text-slate-900 transition hover:bg-red-500 hover:text-white disabled:cursor-not-allowed disabled:opacity-60"
        >
          {loading ? 'Please wait...' : submitLabel}
        </button>
      </form>
      {footer && <div className="mt-6 text-center text-xs text-slate-400">{footer}</div>}
    </div>
  )
}

export default AuthForm
