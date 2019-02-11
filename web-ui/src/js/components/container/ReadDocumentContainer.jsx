import React, { Component } from "react";
import ReactDOM from "react-dom";
import axios from 'axios';
import InputId from "../presentational/InputId.jsx";
import InputButton from "../presentational/InputButton.jsx";
import OutputTraffic from "../presentational/OutputTraffic.jsx";

class ReadDocumentContainer extends Component {

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
    const { id, request, response } = this.state;
    return (
      <span>
        <form id="read-document-form">
          <InputId
            id="read-document-id"
            label="Id:"
            value={id}
            handleChange={this.handleIdChange}
          />
          <InputButton
            id="read-document-button"
            value="Read Document"
            handleClick={this.handleClick}
          />
        </form>
        <OutputTraffic
          requestId="read-document-request"
          requestLabel="Request:"
          requestValue={request}
          responseId="read-document-response"
          responseLabel="Response:"
          responseValue={response}
        />
      </span>
    );
  }
}

export default ReadDocumentContainer;

const wrapper = document.getElementById("read-document-div");
wrapper ? ReactDOM.render(<ReadDocumentContainer />, wrapper) : false;
