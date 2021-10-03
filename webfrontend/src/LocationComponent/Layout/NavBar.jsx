//Modules
import React, { useContext } from "react";
import { Link, useHistory } from "react-router-dom";
import axios from "axios";
import { apiBaseURL } from "../../Config.js";

import './NavBar.styles.css';
// import {Button} from "react-bootstrap";
import UserAuthContext from '../UserAuthContext';

export default function NavBar() {
    const { userLoggedIn, getUserLoggedIn, userName, userEmail } = useContext(UserAuthContext);

    const history = useHistory();

    const handleLogout = async () => {
        await axios.get(`${apiBaseURL}/user/logout`);
        await getUserLoggedIn();
        history.push("/home");
    }
    return (
        <>
            <nav className="navbar navbar-expand-sm navbar-light bg-light">
                <div className="container-fluid">
                    <span className="navbar-brand m-4 h1">
                        <Link className="nav-link logo" to="/">Elate <span>Tourist</span></Link>
                    </span>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup">
                        {/*<span className="navbar-toggler-icon"></span>*/}
                    </button>
                    <div className="collapse navbar-collapse" id="navbarNavAltMarkup">
                        <div className="navbar-nav ms-auto">
                            {(userLoggedIn === false || userLoggedIn === undefined) && (<Link className="nav-link" to="/login" id="login-button">
                                <button type="button">Sign In</button>
                            </Link>)}
                            {userLoggedIn === true && (
                                <>
                                    <h5>{userName}</h5>
                                    <Link className="nav-link" to="/home/bookmark">Bookmarks</Link>
                                    <button type="button" onClick={handleLogout}>Logout</button>
                                </>
                            )}
                            <Link className="nav-link" to="/new">NewLocation</Link>
                        </div>
                    </div>
                </div>
            </nav>
        </>
    )
}