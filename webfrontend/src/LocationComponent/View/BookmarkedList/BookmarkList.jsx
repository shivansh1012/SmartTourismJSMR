import React, { useEffect, useState } from "react";
import axios from "axios";
import { apiBaseURL } from "../../../Config.js";
import { Link } from "react-router-dom";

export default function BookmarkList() {
    const [bookmarkList, setBookmarkList] = useState([]);

    const getBookmarkList = async () => {
        const response = await axios.get(`${apiBaseURL}/user`);
        setBookmarkList(response.data.bookmarkList)
    }

    useEffect(() => {
        getBookmarkList();
    }, []);

    return (
        <>
            {
                bookmarkList.length > 0 && bookmarkList.map((location) => {
                    return (
                        <div className="bookmarked-locations">
                            <div id="back">
                                <Link to="/home" style={{}}>{'<'}Back</Link>
                            </div>
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