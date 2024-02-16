import "../css-files/NotificationPage.css"
import Header from "./Header.tsx";
import {useEffect, useState} from "react";
import axios from "axios";
import {faUser} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {send} from "vite";
function NotificationPage() {
    const [friendRequests, setFriendRequests] = useState([]);
    const userId = localStorage.getItem("loggedInUserId");

    const acceptFriendRequest = async (senderId, requestId) => {
        try {
            const response = await axios.post(`http://localhost:8080/user/follow/${senderId}/${userId}/${requestId}`);
            console.log(response.data);
            setFriendRequests(prevFriendRequests =>
                prevFriendRequests.filter(request => request.requestId !== requestId)
            );
        } catch (error) {
            console.error("Error accepting friend request:", error);
        }
    };

    useEffect(() => {
        const fetchFriendRequests = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/user/get-friend-requests-by-receiver/${userId}`);
                setFriendRequests(response.data);
            } catch (error) {
                console.error('Error fetching friend requests:', error);
            }
        };

        fetchFriendRequests();
    }, [userId]);

    return (
        <>
            <Header/>

            <div className={"centre-notif"}>
                <div className={"title-container-notif"}>
                    <p className={"notif-title-text"}>Notifications</p>
                </div>

                <div className={"notif-list"}>
                    {friendRequests.map(request => (
                        <div key={request.requestId} className="friend-request-container">
                            <FontAwesomeIcon icon={faUser}/>
                            <p>{request.sender.fullName} sent you a friend request</p>
                            <button onClick={() => acceptFriendRequest(request.sender.userId, request.requestId)}>Accept</button>
                        </div>
                    ))}
                </div>
            </div>
        </>
    )
}
export default NotificationPage;