import "../css-files/SearchPage.css"
import Header from "./Header.tsx";
import {faMagnifyingGlass, faUser} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useEffect, useState} from "react";
import axios from "axios";
import {useQuery} from "react-query";
function SearchPage() {
    const [searchInput, setSearchInput] = useState('');
    const [searchResult, setSearchResult] = useState([]);
    const userId = localStorage.getItem("loggedInUserId");
    const [requestsSent, setRequestsSent] = useState(new Set());
    const [friendList, setFriendList] = useState([]);

    const handleSearchInputChange = (event) => {
        setSearchInput(event.target.value);
    };

    const { data } = useQuery(
        ["GETDATA", searchInput],
        async () => {
            const response = await axios.get(
                `http://localhost:8080/user/get-user-by-name/${searchInput}`
            );
            return response.data;
        }
    );

    useEffect(() => {
        if (data) {
            setSearchResult(data);
        }
    }, [data]);

    const sendFriendRequest = async (receiverId) => {
        try {
            const response = await axios.post(
                `http://localhost:8080/user/send-friend-request/${userId}/${receiverId}`
            );
            setRequestsSent((prevRequests) => new Set([...prevRequests, receiverId]));
            console.log(response.data);
            console.log("Friend request sent");
        }
        catch (error) {
            console.error("Error sending friend request:", error);

        }
    };

    const returnFriendList = async () => {
        try {
            const response = await axios.get(
                `http://localhost:8080/user/get-friend-list/${userId}`
            );
            setFriendList(response.data);
            console.log(response.data);
        }
        catch (error) {
            console.error("Error getting friend list:", error);

        }
    }

    useEffect(() => {
        returnFriendList();
    }, []);

    const isFriend = (userId) => {
        return friendList.some(friend => friend.userId === userId);
    };

    return (
        <>
            <Header/>

            <div className={"centre-notif"}>
                <div className={"search-container"}>
                    <input placeholder={"Search"} onChange={handleSearchInputChange}/>
                    <FontAwesomeIcon icon={faMagnifyingGlass} className={"search-btn-search"}/>
                </div>

                <div className={"search-user-list"}>
                    {searchResult.map(user => (
                        <div className={"search-result"} key={user.userId}>
                            <div className={"searched-user-detail"}>
                                <FontAwesomeIcon icon={faUser}/>
                                <p className={"searched-user-name"}>{user.fullName}</p>
                            </div>
                            {isFriend(user.userId) ? (
                                <div className={"friend-label"}>Friend</div>
                            ) : (
                                <button
                                    onClick={() => sendFriendRequest(user.userId)}
                                    disabled={requestsSent.has(user.userId)}
                                >
                                    {requestsSent.has(user.userId) ? "Request Sent" : "Add friend"}
                                </button>
                            )}
                        </div>
                    ))}
                </div>
            </div>
        </>
    )
}
export default SearchPage;