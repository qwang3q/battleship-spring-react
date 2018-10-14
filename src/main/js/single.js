const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

export default class SingleBoard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {cells : []};
    }

    componentDidMount() {
        client({method: 'GET', path: this.props.board._links.cells.href}).done(response => {
            this.setState({cells: response.entity._embedded.cells});
            this.setState({rowCount : this.props.rowCount});
            this.setState({colCount : this.props.colCount});
        });
    }

    render(){
      return (
        <Board cells={this.state.cells} rowCount={this.state.rowCount} colCount={this.state.colCount} />,
        <table>
         				<tbody>
         					<tr><th>{this.state.rowCount}</th></tr>
         				</tbody>
         			</table>
      )
    }
}

