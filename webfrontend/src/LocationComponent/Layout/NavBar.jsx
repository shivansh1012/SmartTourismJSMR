//Modules
import React, {useContext} from "react";
import {Link, useHistory} from "react-router-dom";
import axios from "axios";
import {apiBaseURL} from "../../Config.js";

import './NavBar.styles.css';
import UserAuthContext from '../UserAuthContext';

export default function NavBar() {
    const {userLoggedIn, getUserLoggedIn, userName} = useContext(UserAuthContext);

    const history = useHistory();

    const handleLogout = async () => {
        await axios.get(`${apiBaseURL}/user/logout`);
        await getUserLoggedIn();
        history.push("/home");
    }
    return (
        <>
            <nav className="navbar navbar-light navbar-expand-md navigation-clean" style={{"height": "120px"}}>
                <div className="container">
                    <Link className="nav-link logo" to="/" style={{"fontSize": "40px"}}>Elate <span>Tourist</span></Link>
                    <button data-bs-toggle="collapse" className="navbar-toggler" data-bs-target="#navcol-1">
                        <span className="visually-hidden">Toggle navigation</span>
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navcol-1">
                        <ul className="navbar-nav ms-auto">
                            <Link className="nav-link" to="/home" id="login-button" style={{"fontSize": "20px"}}>Home</Link>
                            {(userLoggedIn === false || userLoggedIn === undefined) && (
                                <>
                                    <Link className="nav-link" to="/login" id="login-button" style={{"fontSize": "20px"}}>Sign In</Link>
                                </>
                                )}
                            {userLoggedIn === true && (
                                <>
                                    <Link className="nav-link" to="/home/bookmark" style={{"fontSize": "20px"}}>Bookmarks</Link>
                                    <li className="nav-item dropdown" style={{"fontSize": "20px"}}>
                                        <a className="dropdown-toggle nav-link" data-bs-toggle="dropdown" href="#">Hello, {userName} </a>
                                        <div className="dropdown-menu">
                                            <button className="dropdown-item" onClick={handleLogout}>Logout</button>
                                        </div>
                                    </li>
                                </>
                            )}

                        </ul>
                    </div>
                </div>
            </nav>
            {/*<nav className="navbar navbar-expand-sm navbar-light bg-light">*/}
            {/*    <div className="container-fluid">*/}
            {/*        <span className="navbar-brand m-4 h1">*/}
            {/*            <Link className="nav-link logo" to="/">Elate <span>Tourist</span></Link>*/}
            {/*        </span>*/}
            {/*        <button className="navbar-toggler" type="button" data-bs-toggle="collapse"*/}
            {/*                data-bs-target="#navbarNavAltMarkup">*/}
            {/*            /!*<span className="navbar-toggler-icon"></span>*!/*/}
            {/*        </button>*/}
            {/*        <div className="collapse navbar-collapse" id="navbarNavAltMarkup">*/}
            {/*            <div className="navbar-nav ms-auto">*/}
            {/*                {(userLoggedIn === false || userLoggedIn === undefined) && (*/}
            {/*                    <Link className="nav-link" to="/login" id="login-button">*/}
            {/*                        <button type="button">Sign In</button>*/}
            {/*                    </Link>)}*/}
            {/*                {userLoggedIn === true && (*/}
            {/*                    <>*/}
            {/*                        <h5>{userName}</h5>*/}
            {/*                        <Link className="nav-link" to="/home/bookmark">Bookmarks</Link>*/}
            {/*                        <button type="button" onClick={handleLogout}>Logout</button>*/}
            {/*                    </>*/}
            {/*                )}*/}
            {/*                <Link className="nav-link" to="/new">NewLocation</Link>*/}
            {/*            </div>*/}
            {/*        </div>*/}
            {/*    </div>*/}
            {/*</nav>*/}
        </>
    )
}