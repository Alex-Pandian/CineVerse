import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import AuthForm from '../components/AuthForm.jsx'
import { useAuth } from '../context/AuthContext.jsx'

const RegisterPage = () => {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const { register, loading, error } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (event) => {
    event.preventDefault()
    const result = await register({ name, email, password })
    if (result.ok) {
      navigate('/')
    }
  }

  return (
    <div className="min-h-screen bg-slate-950 text-white">
      <div className="flex min-h-screen items-center justify-center px-4">
        <AuthForm
          title="Create Account"
          subtitle="Join CineVerse to sync your favorites and personalized recommendations."
          loading={loading}
          error={error}
          submitLabel="Register"
          onSubmit={handleSubmit}
          fields={
            <>
              <div>
                <label className="text-xs text-slate-400">Full Name</label>
                <input
                  type="text"
                  value={name}
                  onChange={(event) => setName(event.target.value)}
                  required
                  className="mt-2 w-full rounded-2xl border border-white/10 bg-transparent px-4 py-3 text-sm text-white outline-none focus:border-red-500"
                />
              </div>
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
              Already have an account?{' '}
              <Link to="/login" className="text-white underline underline-offset-4">
                Log in
              </Link>
            </>
          }
        />
      </div>
    </div>
  )
}

export default RegisterPage
