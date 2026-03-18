import Navbar from './Navbar.jsx'
import Footer from './Footer.jsx'

const Layout = ({ children }) => {
  return (
    <div className="min-h-screen bg-slate-950 text-slate-100">
      <Navbar />
      <main className="px-4 pb-16 pt-8 sm:px-6 lg:px-10">{children}</main>
      <Footer />
    </div>
  )
}

export default Layout
