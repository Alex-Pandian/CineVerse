const SkeletonCard = () => {
  return (
    <div className="overflow-hidden rounded-3xl border border-white/10 bg-white/5">
      <div className="h-72 animate-pulse bg-slate-800/60" />
      <div className="space-y-3 px-4 py-4">
        <div className="h-4 w-3/4 animate-pulse rounded bg-slate-800/60" />
        <div className="h-3 w-1/3 animate-pulse rounded bg-slate-800/60" />
        <div className="h-3 w-full animate-pulse rounded bg-slate-800/60" />
      </div>
    </div>
  )
}

export default SkeletonCard
