import React, { useContext, useState } from "react";
import { useHistory, Link } from "react-router-dom";
import axios from 'axios';
import { apiBaseURL } from "../../../Config.js";
import UserAuthContext from "../../UserAuthContext.js";

export default function UserLogin() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const { getUserLoggedIn } = useContext(UserAuthContext);
    const history = useHistory();

    const handleLogin = async (e) => {
        e.preventDefault();
        const loginData = {
            email,
            password
        }
        await axios.post(`${apiBaseURL}/user/login`, loginData, {
            headers: {
                "Content-Type": "application/json",
            },
            validateStatus: function (status) {
                return status < 500;
            }
        })
            .then(async (res) => {
                if (res.status === 401)
                    alert(res.data.message);
                else {
                    await getUserLoggedIn(true);
                    history.push("/home");
                }
            })
            .catch((err) => {
                console.error(err);
                alert("Error")
            });
    };

    return (
        <div className="container">
            <div className="row">
                <div className="col-md-4 offset-md-4">
                    <div className="login-form bg-light mt-4 p-4">
                        <form className="row g-3" onSubmit={handleLogin}>
                            <div className="col-12">
                                <label>Email</label>
                                <input type="text" name="email" className="form-control" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
                            </div>
                            <div className="col-12">
                                <label>Password</label>
                                <input type="password" name="password" className="form-control" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
                            </div>
                            <div className="col-12">
                                <button type="submit" className="btn btn-dark float-end">Login</button>
                            </div>
                        </form>
                        <hr className="mt-4" />
                        <div className="col-12">
                            <p className="text-center mb-0"><Link to="/register">Signup?</Link></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
