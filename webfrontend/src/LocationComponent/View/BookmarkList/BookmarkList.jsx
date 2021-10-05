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
            <div id="back">
                <Link to="/home" style={{"color":"Black"}}>{'<'}Back</Link>
            </div>
            {
                bookmarkList.length > 0 && bookmarkList.map((location) => {
                    return (
                            <div className="list" key={location.id}>
                                <div className="ui card">
                                    <Link to={`/home/location/${location.id}`}>
                                        <img src={location.imageURL} alt={location.name} />
                                    </Link>

                                    <div className="content">
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
        </>
    )
}