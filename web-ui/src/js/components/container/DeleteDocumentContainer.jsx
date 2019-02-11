import React, { Component } from "react";
import ReactDOM from "react-dom";
import axios from 'axios';
import InputId from "../presentational/InputId.jsx";
import InputButton from "../presentational/InputButton.jsx";
import OutputTraffic from "../presentational/OutputTraffic.jsx";

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
    var request = {
        method: 'DELETE',
        url: window.location.href + 'producer/documents/' + this.state.id,
    }

    this.setState({ request: JSON.stringify(request, null, 4) });

    axios(request).then(response => this.updateResponse(response))
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
        <form id="delete-document-form">
          <InputId
            id="delete-document-id"
            label="Id:"
            value={id}
            handleChange={this.handleIdChange}
          />
          <InputButton
            id="delete-document-button"
            value="Delete Document"
            handleClick={this.handleClick}
          />
        </form>
        <OutputTraffic
          requestId="delete-document-request"
          requestLabel="Request:"
          requestValue={request}
          responseId="delete-document-response"
          responseLabel="Response:"
          responseValue={response}
        />
      </span>
    );
  }
}

export default DeleteDocumentContainer;

const wrapper = document.getElementById("delete-document-div");
wrapper ? ReactDOM.render(<DeleteDocumentContainer />, wrapper) : false;
