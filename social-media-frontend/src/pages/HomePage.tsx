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
    const [recommendedPosts, setRecommendedPosts] = useState([]);

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
                const uniquePosts = friendPostsData.flat().filter((post, index, self) =>
                    index === self.findIndex((p) => p.postId === post.postId)
                );
                setFriendPosts(uniquePosts);
            } catch (error) {
                console.error("Error getting posts of friends:", error);
            }
        };

        returnFriendPosts();
    }, [friendList]);

    useEffect(() => {
        if (userId) {
            const fetchRecommendedPosts = async () => {
                try {
                    const response = await axios.get(`http://localhost:8080/recommend/recommendations/${userId}`);
                    setRecommendedPosts(response.data);
                } catch (error) {
                    console.error("Error getting recommended posts:", error);
                }
            };

            fetchRecommendedPosts();
        }
    }, [userId]);

    const displayPosts = userId && friendList.length > 0 && friendPosts.length > 0 ?
        recommendedPosts.length > 0 ? recommendedPosts : friendPosts :
        [];

    const handleLikePost = async (postId) => {
        try {
            await axios.post(`http://localhost:8080/post/like/${postId}`);
        }
        catch (error) {
            console.error("Error liking post:", error);
        }
    };

    const handleSaveLike = async (postId) => {
        try {
            await axios.post("http://localhost:8080/like/save-like", {
                postId: postId,
                userId: userId,
            });
            // Update UI or fetch updated post data
        } catch (error) {
            console.error("Error saving like:", error);
            // Handle error
        }
    };

    const handleLikeFunctions = (postId) => {
        handleLikePost(postId);
        handleSaveLike(postId);
        window.location.reload();
    }

    return (
        <>
            <Header/>
            <div className={"home-centre"}>
                <p className={"home-title-text"}>Home</p>

                <div className={"home-post-list"}>
                    {displayPosts.map((post) => (
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
                                <div className={"like-container"} onClick={() => handleLikeFunctions(post.postId)}>
                                    <FontAwesomeIcon icon={faThumbsUp}/>
                                    <p>Like</p>
                                    <p>{post.likes}</p>
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