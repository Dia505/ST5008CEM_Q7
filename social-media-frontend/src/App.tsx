import './App.css'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import {QueryClient, QueryClientProvider} from "react-query";
import RegisterPage from "./pages/RegisterPage.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import HomePage from "./pages/HomePage.tsx";
import Header from "./pages/Header.tsx";
import NotificationPage from "./pages/NotificationPage.tsx";
import SearchPage from "./pages/SearchPage.tsx";
import ProfilePage from "./pages/ProfilePage.tsx";
const router = createBrowserRouter(
    [
        {
            path: "/",
            element: <RegisterPage/>
        },
        {
            path: "/login",
            element: <LoginPage/>
        },
        {
            path: "/home",
            element: <HomePage/>
        },
        {
            path: "/header",
            element: <Header/>
        },
        {
            path: "/notification",
            element: <NotificationPage/>
        },
        {
            path: "/search",
            element: <SearchPage/>
        },
        {
            path: "/profile",
            element: <ProfilePage/>
        }
    ]
)
const queryClient = new QueryClient();
function App() {

  return (
    <>
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router}/>
        </QueryClientProvider>
    </>
  )
}

export default App
