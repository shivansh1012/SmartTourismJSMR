import React, { useEffect, useState, useCallback, useContext } from "react";
import axios from "axios";
import { apiBaseURL } from "../../../Config.js";
import { Link } from "react-router-dom";

export default function LocationInfo(props) {
    const [locationInfo, setLocationInfo] = useState([]);
    const [bookmark, setBookmark] = useState(undefined);

    const addToBookmark = async () => {
        const payloadData = {
            locationId: locationInfo.id
        }
        await axios.post(`${apiBaseURL}/user/bookmark`, payloadData, {
            headers: {
                "Content-Type": "application/json",
            },
            validateStatus: function (status) {
                return status < 500;
            }
        })
            .then(async (res) => {
                if (res.status === 401)
                    alert("Please Login First")
                else
                    setBookmark(true);
            })
            .catch((err) => {
                alert("Internal Server Error")
                console.log(err)
            });
    }

    const removeFromBookmark = async () => {
        const payloadData = {
            locationId: locationInfo.id
        }
        await axios.post(`${apiBaseURL}/user/bookmark/remove`, payloadData, {
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
                    alert(res.data.message);
                    setBookmark(false);
                }
            })
            .catch((err) => {
                console.error(err);
                alert("Error")
            });
    }

    const getLocationInfo = useCallback(async () => {
        await axios.get(`${apiBaseURL}/location?id=${props.id}`).then((res) => {
            setLocationInfo(res.data.locationList)
            setBookmark(res.data.isBookmarked);
            props.setPointer(res.data.locationList.name);
            // console.log(res)
        });
    }, [props]);

    useEffect(() => {
        getLocationInfo();
    }, [getLocationInfo]);

    return (
        <div className="container p-4">
            <div className="row">
                <div className="d-flex justify-content-between mb-3">
                    <div id="back">
                        <Link to="/home" style={{textDecoration: "none", color: "black"}}>{'<'} Back</Link>
                    </div>
                    <div className="bookmark-cta">
                        {bookmark === false &&
                        <button type="button" className="bookmark-button" onClick={addToBookmark} style={{width: "160px"}}>
                            Add Bookmark
                        </button>}
                        {bookmark === true &&
                        <button type="button" className="bookmark-button" onClick={removeFromBookmark} style={{width: "160px"}}>
                            Remove Bookmark
                        </button>}
                    </div>
                </div>

                <div className="location-content">
                    <h3 className="d-flex justify-content-center" style={{ "fontFamily": "Poppins", "color": "#043263FF" }}>{locationInfo.name}</h3>
                    <img src={locationInfo.imageURL} alt={locationInfo.name} style={{ "width": "100%" }} />
                    <p>{locationInfo.description}</p>
                </div>
            </div>
        </div>
    )
}