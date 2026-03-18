const EmptyState = ({ title, description, action }) => {
  return (
    <div className="rounded-3xl border border-dashed border-white/10 bg-white/5 p-10 text-center">
      <h3 className="text-lg font-semibold text-white">{title}</h3>
      <p className="mt-2 text-sm text-slate-400">{description}</p>
      {action && <div className="mt-4 flex justify-center">{action}</div>}
    </div>
  )
}

export default EmptyState
