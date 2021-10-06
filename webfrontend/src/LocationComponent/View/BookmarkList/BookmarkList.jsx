import React, { useEffect, useState } from "react";
import axios from "axios";
import { apiBaseURL } from "../../../Config.js";
import { Link } from "react-router-dom";

export default function BookmarkList() {
    const [bookmarkList, setBookmarkList] = useState([]);

    const getBookmarkList = async () => {
        const response = await axios.get(`${apiBaseURL}/user/bookmark`);
        setBookmarkList(response.data.bookmarkList)
    }

    useEffect(() => {
        getBookmarkList();
    }, []);

    return (
        <>
            <div className="container px-5">
                <div className="row">
                    <div className="d-flex justify-content-between">
                        <div id="back">
                            <Link to="/home" style={{ "color": "Black", "text-decoration": "none" }}>{'<'}Back</Link>
                        </div>
                        <p style={{ "fontSize": "25px", "font-weight": "500" }}>Bookmarks</p>
                        <p style={{ "color": "white" }}>Space</p>
                    </div>
                    <div>
                        {
                            bookmarkList.length > 0 && bookmarkList.map((location) => {
                                return (
                                    <div className="list" key={location.id}>
                                        <div className="card">
                                            <Link to={`/home/location/${location.id}`}>
                                                <img src={location.imageURL} alt={location.name} className="p-3" />
                                            </Link>

                                            <div className="content text-center">
                                                <Link to={`/home/location/${location.id}`}>
                                                    {location.name}
                                                </Link>
                                            </div>
                                        </div>
                                    </div>
                                )
                            })
                        }

                        {
                            bookmarkList.length === 0 && <p>No Bookmarks</p>
                        }
                    </div>
                </div>
            </div>
        </>
    )
}