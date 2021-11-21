import React, { useContext, useState } from "react";
import {Link, useHistory} from "react-router-dom";
import axios from 'axios';
import { apiBaseURL } from "../../../Config.js";
import UserAuthContext from "../../UserAuthContext.js";

import "./UserRegister.styles.css";

export default function UserRegister() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const { getUserLoggedIn } = useContext(UserAuthContext);
    const history = useHistory();

    const handleRegister = async (e) => {
        e.preventDefault();
        const registerData = {
            name,
            email,
            password
        }
        axios.post(`${apiBaseURL}/user/register`, registerData, {
            headers: {
                "Content-Type": "application/json",
            }
        })
            .then(async (res) => {
                alert(res.data.message);
                await getUserLoggedIn();
                history.push("/login");
            })
            .catch((err) => {
                console.error(err);
                alert("Error")
            });
    };

    return (
        <div className="signUp-form">
            <form onSubmit={handleRegister}>
                <h2>Sign Up</h2>
                <div className="form-container">
                    <label>Name</label>
                    <input type="text" name="name" className="form-control" placeholder="Name" value={name} onChange={(e) => setName(e.target.value)} />

                    <label>Email</label>
                    <input type="text" name="email" className="form-control" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />

                    <label>Password</label>
                    <input type="password" name="password" className="form-control" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />

                    <div className="text-center">
                        <button type="submit" className="btn btn-success me-2">Sign Up</button>
                    </div>
                </div>
            </form>
            <p className="signIn-cta"><Link to="/login">Already have an account? Sign In</Link></p>
        </div>
    )
}
