import React, { Component } from "react";
import ReactDOM from "react-dom";
import axios from 'axios';
import InputId from "../presentational/InputId.jsx";
import InputButton from "../presentational/InputButton.jsx";
import OutputRequest from "../presentational/OutputRequest.jsx";
import OutputResponse from "../presentational/OutputResponse.jsx";
import PictureContainer from "./PictureContainer.jsx";

class DeleteDocumentContainer extends Component {

  constructor() {
    super();

    this.state = {
      id: "",

      request: "",
      response: ""
    };

    this.handleIdChange = this.handleIdChange.bind(this);
    this.handleClick = this.handleClick.bind(this);
  }

  handleIdChange(event) {
    this.setState({ id: event.target.value });
  }

  handleClick(event) {
    this.setState({ response: 'Waiting...' })

    PictureContainer.activateFlow('flow-delete');

    var request = {
        method: 'DELETE',
        url: window.location.origin + '/producer/documents/' + this.state.id,
    }

    this.setState({ request: JSON.stringify(request, null, 4) });

    axios(request)
        .then(response => this.updateResponse(response))
        .catch(error => this.updateResponse(error.response))
  }

  updateResponse(response) {
    this.setState({
        response: JSON.stringify({
            status: response.status,
            headers: response.headers,
            data: response.data
        }, null, 4)
    });
  }

  render() {
    const { id, request, response } = this.state;
    return (
      <div className="view-container">
        <div className="view-title">Delete document</div>
        <InputId
          id="delete-document-id"
          value={id}
          handleChange={this.handleIdChange}
        />
        <InputButton
          id="delete-document-button"
          value="Delete Document"
          handleClick={this.handleClick}
        />
        <OutputRequest
          id="delete-document-request"
          value={request}
        />
        <OutputResponse
          id="delete-document-response"
          value={response}
        />
      </div>
    );
  }
}

export default DeleteDocumentContainer;

const wrapper = document.getElementById("delete-document-div");
wrapper ? ReactDOM.render(<DeleteDocumentContainer />, wrapper) : false;
