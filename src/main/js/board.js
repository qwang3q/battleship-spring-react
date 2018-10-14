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
            <Cell key={cell.id} cell={cell} cellUpdate={this.cellUpdate} type="User" />    
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

class ComputerFleetBoard extends React.Component {

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
        client({method: 'GET', path: '/board?type=computerFleetBoard'}).done(response => {
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
            <Cell key={cell.id} cell={cell} cellUpdate={this.cellUpdate}  type="Computer" />    
        )

        return (
        <div className="game-board">
            <div className="status">ComputerFleetBoard</div>
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
        let stateVal = this.props.cell.sunk == true ? "." : this.props.cell.hit == true ? "X" : this.props.cell.shipCell == true ? "S" : "-";
        let cumtomCss = "invisible"
        if(this.props.type == "User") {
            cumtomCss = this.props.cell.hit == true ? "visible" : this.props.cell.shipCell == true ? "visible" : "invisible";
        } else {
            cumtomCss = this.props.cell.hit == false ? "invisible" : "visible";
        }
        let className = "square board-cell " + cumtomCss;
        return (
            <button
                className={className}
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

ReactDOM.render(
	<ComputerFleetBoard />,
	document.getElementById('computerFleetBoard')
)
