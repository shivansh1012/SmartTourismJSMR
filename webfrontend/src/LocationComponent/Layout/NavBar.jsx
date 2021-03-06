//Modules
import React, { useContext } from "react";
import { Link, useHistory } from "react-router-dom";
import axios from "axios";
import { apiBaseURL } from "../../Config.js";

import './NavBar.styles.css';
import UserAuthContext from '../UserAuthContext';

export default function NavBar() {
    const { userLoggedIn, getUserLoggedIn, userName } = useContext(UserAuthContext);

    const history = useHistory();

    const handleLogout = async () => {
        await axios.get(`${apiBaseURL}/user/logout`);
        await getUserLoggedIn();
        history.push("/home");
    }
    return (
        <>
            <nav className="navbar navbar-light navbar-expand-md navigation-clean">
                <div className="container">
                    <Link className="nav-link logo py-4" to="/home" style={{ "fontSize": "40px" }}>Elate <span>Tourist</span></Link>
                    <button data-bs-toggle="collapse" className="navbar-toggler" data-bs-target="#navcol-1">
                        <span className="visually-hidden">Toggle navigation</span>
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navcol-1">
                        <ul className="navbar-nav ms-auto">
                            <div className="homepageBTN">
                                <Link className="nav-link" to="/home" id="login-button" style={{ "fontSize": "20px" }}>Home</Link>
                            </div>
                            {(userLoggedIn === false || userLoggedIn === undefined) && (
                                <div className="signInBTN">
                                    <Link className="nav-link" to="/login" id="login-button" style={{ "fontSize": "20px" }}>Sign In</Link>
                                </div>
                            )}
                            {userLoggedIn === true && (
                                <>
                                    <div className="bookmarkBTN">
                                        <Link className="nav-link" to="/home/bookmark" style={{ "fontSize": "20px" }}>Bookmarks</Link>
                                    </div>
                                    <li className="nav-item dropdown" style={{ "fontSize": "20px" }}>
                                        <div className="dropdown-toggle nav-link homepageBTN" data-bs-toggle="dropdown" style={{cursor:"pointer"}}>Hello, {userName} </div>
                                        <div className="dropdown-menu">
                                            <button className="dropdown-item" disabled>Preferences (Coming Soon)</button>
                                            <button className="dropdown-item" onClick={handleLogout}>Logout</button>
                                        </div>
                                    </li>
                                </>
                            )}
                        </ul>
                    </div>
                </div>
            </nav>
        </>
    )
}