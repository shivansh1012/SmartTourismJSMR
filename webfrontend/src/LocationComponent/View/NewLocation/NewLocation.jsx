import React, { useState } from "react";
import axios from 'axios';
import { apiBaseURL } from "../../../Config.js";
import { useHistory } from "react-router-dom";

export default function NewLocation() {
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [imageURL, setImageURL] = useState("");
    const [address, setAddress] = useState("");
    const [state, setState] = useState("");
    const [country, setCountry] = useState("");

    let history = useHistory();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const newLocation = {
            name,
            description,
            imageURL,
            address,
            state,
            country
        }
        axios.post(`${apiBaseURL}/location`, newLocation, {
            headers: {
                "Content-Type": "application/json",
            }
        })
            .then(async (res) => {
                alert(res.data.message);
                history.push("/");
            })
            .catch((err) => {
                console.error(err);
                alert("Error")
            });
    };

    return (
        <div>
            <div className="container">
                <div className="row">
                    <div>
                        <div className="news-form bg-light mt-4 p-4">
                            <form className="row g-3" onSubmit={handleSubmit}>
                                <div className="col-12">
                                    <label>Name</label>
                                    <input type="text" name="name" className="form-control" placeholder="Name" value={name} onChange={(e) => setName(e.target.value)} />
                                </div>
                                <div className="col-12">
                                    <label>ImageURL</label>
                                    <input type="text" name="img" className="form-control" placeholder="ImageURL" value={imageURL} onChange={(e) => setImageURL(e.target.value)}/>
                                </div>
                                <div className="col-12">
                                    <label>Description</label>
                                    <textarea type="text" name="description" className="form-control" placeholder="Description" value={description} onChange={(e) => setDescription(e.target.value)} style={{ height: "30vh" }} />
                                </div>
                                <div className="col-12">
                                    <label>Address</label>
                                    <textarea type="text" name="location" className="form-control" placeholder="Address" value={address} onChange={(e) => setAddress(e.target.value)} style={{ height: "10vh" }} />
                                </div>
                                <div className="col-12">
                                    <label>State</label>
                                    <input type="text" name="location" className="form-control" placeholder="State" value={state} onChange={(e) => setState(e.target.value)}/>
                                </div>
                                <div className="col-12">
                                    <label>Country</label>
                                    <input type="text" name="location" className="form-control" placeholder="Country" value={country} onChange={(e) => setCountry(e.target.value)}/>
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