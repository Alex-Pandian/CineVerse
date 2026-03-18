import { Link } from 'react-router-dom'
import Layout from '../components/Layout.jsx'

const NotFoundPage = () => {
  return (
    <Layout>
      <div className="mx-auto flex max-w-xl flex-col items-center gap-4 rounded-3xl border border-white/10 bg-white/5 p-10 text-center">
        <h1 className="text-3xl font-semibold text-white">Page Not Found</h1>
        <p className="text-sm text-slate-400">The page you are looking for does not exist.</p>
        <Link
          to="/"
          className="rounded-full bg-white px-4 py-2 text-sm font-semibold text-slate-900 transition hover:bg-red-500 hover:text-white"
        >
          Go Home
        </Link>
      </div>
    </Layout>
  )
}

export default NotFoundPage
