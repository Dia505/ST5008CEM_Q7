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

    // To update the searchInput state variable based on the user's entry in search bar
    const handleSearchInputChange = (event) => {
        setSearchInput(event.target.value);
    };

    // To display a user based on their full name
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
            // Sets data of search user in searchResult state variable
            setSearchResult(data);
        }
    }, [data]);

    // To send friend request to that user
    const sendFriendRequest = async (receiverId) => {
        try {
            const response = await axios.post(
                `http://localhost:8080/user/send-friend-request/${userId}/${receiverId}`
            );
            // To update state variable requestSent by creating a new set with the previous request sent and receiver id
            // Taking previous requests as argument ensures that we are working with the most up-to-date state
            setRequestsSent((prevRequests) => new Set([...prevRequests, receiverId]));
            console.log(response.data);
            console.log("Friend request sent");
        }
        catch (error) {
            console.error("Error sending friend request:", error);

        }
    };

    // To return friend list and display friend on searching that user
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

    // To check if the users are friends of the logged-in user
    const isFriend = (userId) => {
        // some method iterate over each element of friendList array
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
                            {/*If friend, "Friend" label is displayed*/}
                            {isFriend(user.userId) ? (
                                <div className={"friend-label"}>Friend</div>
                            ) : (
                                // If not friend, "Add friend" button is displayed
                                // On clicking the button, "Request sent" is displayed and the button becomes unclickable
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