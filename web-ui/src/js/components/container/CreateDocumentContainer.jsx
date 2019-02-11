import React, { Component } from "react";
import ReactDOM from "react-dom";
import axios from 'axios';
import InputAuthor from "../presentational/InputAuthor.jsx";
import InputText from "../presentational/InputText.jsx";
import InputButton from "../presentational/InputButton.jsx";
import OutputId from "../presentational/OutputId.jsx";
import OutputTraffic from "../presentational/OutputTraffic.jsx";

class CreateDocumentContainer extends Component {

  constructor() {
    super();

    this.state = {
      id: "",
      author: "",
      text: "",

      request: "",
      response: ""
    };

    this.handleAuthorChange = this.handleAuthorChange.bind(this);
    this.handleTextChange = this.handleTextChange.bind(this);
    this.handleClick = this.handleClick.bind(this);
  }

  handleAuthorChange(event) {
    this.setState({ author: event.target.value })
  }

  handleTextChange(event) {
    this.setState({ text: event.target.value })
  }

  handleClick(event) {
    var request = {
        method: 'POST',
        url: window.location.href + 'producer/documents',
        headers: {
            'Content-Type': 'application/vnd.kafka-cqrs.create-document.1+json'
        },
        data: {
            author: this.state.author,
            text: this.state.text
        }
    }

    this.setState({ request: JSON.stringify(request, null, 4) });

    axios(request)
        .then(response => this.updateResponse(response))
        .catch(error => this.updateError(error.response))
  }

  updateResponse(response) {
    this.setState({
        id: response.data.id,
        response: JSON.stringify({
            status: response.status,
            headers: response.headers,
            data: response.data
        }, null, 4)
    });
  }

  updateError(response) {
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
      <span>
        <form id="create-document-form">
          <InputAuthor
            id="create-document-author"
            label="Author:"
            value={author}
            handleChange={this.handleAuthorChange}
          />
          <InputText
            id="create-document-text"
            label="Text:"
            value={text}
            handleChange={this.handleTextChange}
          />
          <InputButton
            id="create-document-button"
            value="Create Document"
            handleClick={this.handleClick}
          />
        </form>
        <OutputId
          id="create-document-id"
          label="Id:"
          value={id}
        />
        <OutputTraffic
          requestId="create-document-request"
          requestLabel="Request:"
          requestValue={request}
          responseId="create-document-response"
          responseLabel="Response:"
          responseValue={response}
        />
      </span>
    );
  }
}

export default CreateDocumentContainer;

const wrapper = document.getElementById("create-document-div");
wrapper ? ReactDOM.render(<CreateDocumentContainer />, wrapper) : false;
