const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');


class UserFleetBoard extends React.Component {

	constructor(props) {
		super(props);
        this.state = { 
            height : 10,
            width: 10,
            cells: [],
            rows: []
        };

        this.loadFromServer = this.loadFromServer.bind(this);
        this.cellUpdate = this.cellUpdate.bind(this);
	}

    loadFromServer() {
        client({method: 'GET', path: '/board?type=userFleetBoard'}).done(response => {
            this.setState({cells: response.entity.cells });
        });
    }

    cellUpdate(cell) {
        this.loadFromServer();
    }

	componentDidMount() {
        this.loadFromServer();
	}

	render() {
        const foobar = "   ";
        const map = this.state.cells.map ( cell =>
            <Cell key={cell.id} cell={cell} cellUpdate={this.cellUpdate} />    
        )

        return (
        <div className="game-board">
            <div className="status">UserFleetBoard</div>
            {map}
            <div className="status">{foobar}</div>
        </div>
        )
    }
}

class Cell extends React.Component {
    constructor(props) {
        super(props);
        this.handleHit = this.handleHit.bind(this);
    }

    handleHit() {
        client({method: 'GET', path: '/hitcell?id=' + this.props.cell.id }).done(response => {

        });
        this.props.cellUpdate(this.props.cell);
    }

    render() {
        let stateVal = this.props.cell.sunk == true ? "." : this.props.cell.hit == true ? "X" : this.props.cell.shipCell == true ? "S" : " ";
        return (
            <button
                className="square"
                className="board-cell"
                onClick={this.handleHit}
            >
              {stateVal}
            </button>
        )
    }
}




ReactDOM.render(
	<UserFleetBoard />,
	document.getElementById('userFleetBoard')
)

// ReactDOM.render(
// 	<ComputerFleetBoard />,
// 	document.getElementById('computerFleetBoard')
// )
