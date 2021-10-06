import React, { useEffect, useState, useCallback } from "react";
import axios from "axios";
import { apiBaseURL } from "../../../Config.js";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBookmark } from "@fortawesome/free-regular-svg-icons";
import { faStar } from "@fortawesome/free-regular-svg-icons";

export default function LocationInfo(props) {
    const [locationInfo, setLocationInfo] = useState([]);
    const [bookmark, setBookmark] = useState(undefined);
    const [reviewList, setReviewList] = useState([]);

    const [review, setReview] = useState("");
    const [rating, setRating] = useState(0);

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

    const handleSubmit = async (e) => {
        e.preventDefault();
        const payloadData = {
            locationId: locationInfo.id,
            rating: rating,
            reviewText: review
        }
        await axios.post(`${apiBaseURL}/review`, payloadData, {
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
                    // alert(res.data.message);
                    getLocationInfo();
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
            // console.log(res.data.locationList);
            setReviewList(res.data.locationList.review);
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
                        <Link to="/home" style={{ textDecoration: "none", color: "black" }}>{'<'} Back</Link>
                    </div>
                    <h3 style={{ "fontFamily": "Poppins", "color": "#043263FF" }}>
                        {locationInfo.name}
                    </h3>
                    <div className="bookmark-cta">
                        {bookmark === false &&
                            <p type="button" className="bookmark-button" onClick={addToBookmark}>
                                <FontAwesomeIcon icon={faBookmark} /> Save<span style={{"color": "white"}}>me</span></p>
                        }
                        {bookmark === true &&
                            <p type="button" className="bookmark-button" onClick={removeFromBookmark}>
                                <FontAwesomeIcon icon={faBookmark} /> Remove</p>
                        }
                    </div>
                </div>

                <div className="location-content">
                    {/* <h3 className="d-flex justify-content-center" style={{ "fontFamily": "Poppins", "color": "#043263FF" }}>{locationInfo.name}</h3> */}
                    <img src={locationInfo.imageURL} alt={locationInfo.name} style={{ "width": "100%" }} />
                    <p className="py-3"> {locationInfo.description}</p>
                    <p style={{ "fontSize": "20px" }}> Reviews</p>
                    <div className="row">
                        {
                            reviewList.length === 0 &&
                            <p>No Reviews Yet. Be the first one.</p>
                        }
                        {
                            reviewList.length !== 0 && reviewList.map((review) => {
                                return (
                                    <div className="col-sm-6 py-2" key={review.id}>
                                        <div className="card">
                                            <div className="card-body">
                                                <p className="card-text">{review.review} - {review.rating} <FontAwesomeIcon icon={faStar} /></p>
                                            </div>
                                        </div>
                                    </div>
                                )
                            })
                        }
                    </div>
                    <div className="container">
                        <div className="row">
                            <form className="row g-3" onSubmit={handleSubmit}>
                                <div className="col-12">
                                    <label>Review</label>
                                    <textarea type="text" name="Review" className="form-control" placeholder="Write a Review" value={review} onChange={(e) => setReview(e.target.value)} style={{ height: "10vh" }} />
                                </div>
                                <div className="col-12">
                                    <label>Rating</label>
                                    <input type="text" name="rating" className="form-control" placeholder="Rate it" value={rating} onChange={(e) => setRating(e.target.value)} />
                                </div>
                                <div className="col-12">
                                    <button type="submit" className="btn btn-dark float-end">Submit</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}