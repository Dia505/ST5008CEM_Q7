import {Link, useNavigate} from "react-router-dom";
import "../css-files/RegisterPage.css"
import { useMutation } from "react-query";
import axios from "axios";
import {useForm} from "react-hook-form";
function RegisterPage() {
    const navigate = useNavigate();

    const saveUser = useMutation({
        mutationKey:"SAVEUSER",
        mutationFn:(requestData:any)=> {
            console.log(requestData)
            return axios.post("http://localhost:8080/user/save-user",requestData)
        },
        onSuccess: () => {
            alert("Account created")
            navigate("/login");
        }
    })

    const {
        register,
        handleSubmit
    } = useForm();

    const onSubmit = (values:unknown) => {
        saveUser.mutate((values))
    }

    return (
        <>
            <div className={"centre-register"}>
                <div className={"title-container-register"}>
                    <p className={"register-title-text"}>Register</p>
                </div>

                <form onSubmit={handleSubmit(onSubmit)}>
                    <div className={"register-form-container"}>
                        <div className={"register-text-field-container"}>
                            <div>
                                <label>Full name: </label>
                                <input {...register("fullName")}/>
                            </div>
                            <div>
                                <label>Address: </label>
                                <input {...register("address")}/>
                            </div>
                            <div>
                                <label>Email address: </label>
                                <input {...register("email")}/>
                            </div>
                            <div>
                                <label>Password: </label>
                                <input type={"password"} {...register("password")}/>
                            </div>
                        </div>

                        <div className={"register-btn-container"}>
                            <button className={"register-btn"} type={"submit"}>Create account</button>
                        </div>

                        <div className={"login-container-register"}>
                            <p>Already have an account?</p>
                            <Link to={"/login"}><p className={"login-btn-register"}>Login</p></Link>
                        </div>
                    </div>
                </form>
            </div>
        </>
    )
}
export default RegisterPage;