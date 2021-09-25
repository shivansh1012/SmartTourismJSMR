import React from 'react';
import { Link } from "react-router-dom";

import './NavBar.styles.css';
// import {Button} from "react-bootstrap";

export default function NavBar() {
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
                            <Link className="nav-link" to="/login" id="login-button">
                                <button type="button">Login</button>
                            </Link>
                            <Link className="nav-link" to="/new">NewLocation</Link>
                        </div>
                    </div>
                </div>
            </nav>
        </>
    )
}