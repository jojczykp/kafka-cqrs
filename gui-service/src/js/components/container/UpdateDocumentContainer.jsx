import React, { Component } from "react";
import ReactDOM from "react-dom";
import axios from 'axios';
import InputId from "../presentational/InputId.jsx";
import InputAuthor from "../presentational/InputAuthor.jsx";
import InputText from "../presentational/InputText.jsx";
import InputButton from "../presentational/InputButton.jsx";
import OutputRequest from "../presentational/OutputRequest.jsx";
import OutputResponse from "../presentational/OutputResponse.jsx";
import PictureContainer from "./PictureContainer.jsx";

class UpdateDocumentContainer extends Component {

  constructor() {
    super();

    this.state = {
      id: "",
      author: "",
      text: "",

      request: "",
      response: ""
    };

    this.handleIdChange = this.handleIdChange.bind(this);
    this.handleAuthorChange = this.handleAuthorChange.bind(this);
    this.handleTextChange = this.handleTextChange.bind(this);
    this.handleClick = this.handleClick.bind(this);
  }

  handleIdChange(event) {
    this.setState({ id: event.target.value });
  }

  handleAuthorChange(event) {
    this.setState({ author: event.target.value });
  }

  handleTextChange(event) {
    this.setState({ text: event.target.value });
  }

  handleClick(event) {
    this.setState({ response: 'Waiting...' })

    PictureContainer.activateFlow('flow-update');

    var request = {
        method: 'PUT',
        url: window.location.origin + '/producer/documents',
        headers: {
            'Content-Type': 'application/vnd.kafka-cqrs.update-document.1+json'
        },
        data: {
            id: this.state.id,
            author: this.state.author,
            text: this.state.text
        }
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
    const { id, author, text, request, response } = this.state;
    return (
      <div className="view-container">
        <div className="view-title">Update document</div>
        <InputId
          id="update-document-id"
          value={id}
          handleChange={this.handleIdChange}
        />
        <InputAuthor
          id="update-document-author"
          value={author}
          handleChange={this.handleAuthorChange}
        />
        <InputText
          id="update-document-text"
          value={text}
          handleChange={this.handleTextChange}
        />
        <InputButton
          id="update-document-button"
          value="Update Document"
          handleClick={this.handleClick}
        />
        <OutputRequest
          id="update-document-request"
          value={request}
        />
        <OutputResponse
          id="update-document-response"
          value={response}
        />
      </div>
    );
  }
}

export default UpdateDocumentContainer;

const wrapper = document.getElementById("update-document-div");
wrapper ? ReactDOM.render(<UpdateDocumentContainer />, wrapper) : false;
