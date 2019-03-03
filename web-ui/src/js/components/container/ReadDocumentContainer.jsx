import React, { Component } from "react";
import ReactDOM from "react-dom";
import axios from 'axios';
import InputId from "../presentational/InputId.jsx";
import InputButton from "../presentational/InputButton.jsx";
import OutputAuthor from "../presentational/OutputAuthor.jsx";
import OutputText from "../presentational/OutputText.jsx";
import OutputRequest from "../presentational/OutputRequest.jsx";
import OutputResponse from "../presentational/OutputResponse.jsx";

class ReadDocumentContainer extends Component {

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
    this.handleClick = this.handleClick.bind(this);
  }

  handleIdChange(event) {
    this.setState({ id: event.target.value });
  }

  handleClick(event) {
    this.setState({ response: 'Waiting...' })

    var request = {
        method: 'GET',
        url: window.location.href + 'reader/documents/' + this.state.id,
        headers: {
            'Accept': 'application/vnd.kafka-cqrs.document.1+json'
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
      <div className="action-container">
        <InputId
          id="read-document-id"
          label="Id:"
          value={id}
          handleChange={this.handleIdChange}
        />
        <OutputAuthor
          id="read-document-author"
          label="Author:"
          value={author}
        />
        <OutputText
          id="read-document-text"
          label="Text:"
          value={text}
        />
        <InputButton
          id="read-document-button"
          value="Read Document"
          handleClick={this.handleClick}
        />
        <OutputRequest
          id="read-document-request"
          label="Request:"
          value={request}
        />
        <OutputResponse
          id="read-document-response"
          label="Response:"
          value={response}
        />
      </div>
    );
  }
}

export default ReadDocumentContainer;

const wrapper = document.getElementById("read-document-div");
wrapper ? ReactDOM.render(<ReadDocumentContainer />, wrapper) : false;
