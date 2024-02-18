import "../css-files/HomePage.css"
import Header from "./Header.tsx";
import axios from "axios";
import {useEffect, useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faThumbsUp, faUser} from "@fortawesome/free-solid-svg-icons";
function HomePage() {
    const userId = localStorage.getItem("loggedInUserId");
    const [friendList, setFriendList] = useState([]);
    const [friendPosts, setFriendPosts] = useState([]);
    const [postLike, setPostLike] = useState(0);

    useEffect(() => {
        const returnFriendList = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/user/get-friend-list/${userId}`);
                setFriendList(response.data);
            } catch (error) {
                console.error("Error getting friend list:", error);
            }
        };

        returnFriendList();
    }, [userId]); // Fetch friend list when userId changes

    useEffect(() => {
        const returnFriendPosts = async () => {
            try {
                const promises = friendList.map(async (friend) => {
                    const response = await axios.get(`http://localhost:8080/post/get-post-by-userId/${friend.userId}`);
                    return response.data;
                });
                const friendPostsData = await Promise.all(promises);
                setFriendPosts(friendPostsData.flat());
                console.log("friendPosts",friendPosts);
            } catch (error) {
                console.error("Error getting posts of friends:", error);
            }
        };

        returnFriendPosts();
    }, [friendList]);

    return (
        <>
            <Header/>
            <div className={"home-centre"}>
                <p className={"home-title-text"}>Home</p>

                <div className={"home-post-list"}>
                    {friendPosts.map((post) => (
                        <div className={"home-post-container"} key={post.postId}>
                            <div className={"home-post-user"}>
                                <FontAwesomeIcon icon={faUser} className={"profile-icon"}/>
                                <div className={"user-name-container"}>
                                    <p>{post.user.fullName}</p>
                                </div>
                            </div>
                            <div className={"home-post-details"}>
                                <p>{post.title}</p>
                                <p className={"home-post-hashtag"}>{post.hashtag}</p>
                                <img className={"home-post"} src={`/${post.postImage}`}/>
                                <div className={"like-container"}>
                                    <FontAwesomeIcon icon={faThumbsUp} />
                                    <p>Like</p>
                                    <p>{postLike}</p>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </>
    )
}
export default HomePage;