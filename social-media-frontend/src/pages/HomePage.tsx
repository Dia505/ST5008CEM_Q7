import "../css-files/HomePage.css"
import Header from "./Header.tsx";
function HomePage() {
    console.log("Logged in user id: ",localStorage.getItem("loggedInUserId"));
    return (
        <>
            <Header/>
        </>
    )
}
export default HomePage;