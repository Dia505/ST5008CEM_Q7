import "../css-files/Login.css"
import { useMutation } from "react-query";
import axios from "axios";
import {useForm} from "react-hook-form";
import { useNavigate } from "react-router-dom";
function LoginPage() {
    const navigate = useNavigate();

    const login = useMutation({
        mutationKey:"LOGIN",
        mutationFn:(requestData:any)=> {
            console.log(requestData)
            return axios.post("http://localhost:8080/login",requestData)
        },
        onSuccess: (response) => {
            alert("Logged in")
            console.log(response.data);
            const userId = response.data;
            localStorage.setItem("loggedInUserId", userId);

            navigate("/home");
        }
    })

    const {
        register,
        handleSubmit
    } = useForm();

    const onSubmit = (values:unknown) => {
        login.mutate((values))
    }
    return (
        <>
            <div className={"centre-login"}>
                <div className={"title-container-login"}>
                    <p className={"login-title-text"}>Login</p>
                </div>

                <form onSubmit={handleSubmit(onSubmit)}>
                    <div className={"login-form-container"}>
                        <div className={"login-text-field-container"}>
                            <div>
                                <label>Email address: </label>
                                <input {...register("email")}/>
                            </div>
                            <div>
                                <label>Password: </label>
                                <input type={"password"} {...register("password")}/>
                            </div>
                        </div>

                        <div className={"login-btn-container"}>
                            <button className={"login-btn"} type={"submit"}>Login</button>
                        </div>
                    </div>
                </form>
            </div>
        </>
    )
}
export default LoginPage;