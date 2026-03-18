const SectionHeader = ({ title, subtitle, action }) => {
  return (
    <div className="flex flex-wrap items-end justify-between gap-3">
      <div>
        <h2 className="text-2xl font-semibold text-white md:text-3xl">{title}</h2>
        {subtitle && <p className="mt-2 text-sm text-slate-400">{subtitle}</p>}
      </div>
      {action && <div>{action}</div>}
    </div>
  )
}

export default SectionHeader
