import { useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import AuthForm from '../components/AuthForm.jsx'
import { useAuth } from '../context/AuthContext.jsx'

const LoginPage = () => {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const { login, loading, error } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const handleSubmit = async (event) => {
    event.preventDefault()
    const result = await login({ email, password })
    if (result.ok) {
      const redirectTo = location.state?.from?.pathname || '/'
      navigate(redirectTo, { replace: true })
    }
  }

  return (
    <div className="min-h-screen bg-slate-950 text-white">
      <div className="flex min-h-screen items-center justify-center px-4">
        <AuthForm
          title="Welcome Back"
          subtitle="Log in to access your favorites, history, and recommendations."
          loading={loading}
          error={error}
          submitLabel="Login"
          onSubmit={handleSubmit}
          fields={
            <>
              <div>
                <label className="text-xs text-slate-400">Email</label>
                <input
                  type="email"
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                  required
                  className="mt-2 w-full rounded-2xl border border-white/10 bg-transparent px-4 py-3 text-sm text-white outline-none focus:border-red-500"
                />
              </div>
              <div>
                <label className="text-xs text-slate-400">Password</label>
                <input
                  type="password"
                  value={password}
                  onChange={(event) => setPassword(event.target.value)}
                  required
                  className="mt-2 w-full rounded-2xl border border-white/10 bg-transparent px-4 py-3 text-sm text-white outline-none focus:border-red-500"
                />
              </div>
            </>
          }
          footer={
            <>
              New here?{' '}
              <Link to="/register" className="text-white underline underline-offset-4">
                Create an account
              </Link>
            </>
          }
        />
      </div>
    </div>
  )
}

export default LoginPage
