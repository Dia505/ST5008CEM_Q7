import "../css-files/Header.css"
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHouse} from "@fortawesome/free-solid-svg-icons";
import {faMagnifyingGlass} from "@fortawesome/free-solid-svg-icons";
import {faUser} from "@fortawesome/free-solid-svg-icons";
import {faBell} from "@fortawesome/free-solid-svg-icons";
import {Link} from "react-router-dom";
function Header() {
    return (
        <>
            <div className={"header-container"}>
                <Link to={"/home"}><FontAwesomeIcon icon={faHouse} className={"home-btn-header"}/></Link>
                <Link to={"/search"}><FontAwesomeIcon icon={faMagnifyingGlass} className={"search-btn-header"}/></Link>
                <Link to={"/notification"}><FontAwesomeIcon icon={faBell} className={"notif-btn-header"}/></Link>
                <FontAwesomeIcon icon={faUser} className={"profile-btn-header"}/>
            </div>
        </>
    )
}
export default Header;