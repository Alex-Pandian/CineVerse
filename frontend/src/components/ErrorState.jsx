const ErrorState = ({ message, action }) => {
  return (
    <div className="rounded-3xl border border-red-500/30 bg-red-500/10 p-6 text-sm text-red-200">
      <p>{message}</p>
      {action && <div className="mt-4">{action}</div>}
    </div>
  )
}

export default ErrorState
