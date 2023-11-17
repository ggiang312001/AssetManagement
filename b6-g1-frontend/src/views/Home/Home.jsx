import React, { useState, useEffect, useCallback } from 'react';
import { Table, Divider, Modal } from 'antd';
import { CloseCircleOutlined } from '@ant-design/icons';
import { ReloadOutlined } from '@ant-design/icons';
import { Button, message } from 'antd';
import { CheckOutlined } from '@ant-design/icons';
import {
	acceptAssignment,
	declineAssignment,
	getAssignmentOfStaff,
	getStaffAssignmentDetail,
} from '../../services/assignmentService';
import { postReturnReq, postReturnReqByAdmin } from '../../services/returnRequestService';
import { AuthContext } from '../../contexts/AuthContext';
import { useContext } from 'react';
//import { useNavigate } from "react-router-dom";
const Home = () => {
	//const navigate = useNavigate();
	const [loading, setLoading] = useState(false);
	const [dataSource, setDataSource] = useState([]);
	const [list, setList] = useState([]);
	const [totalElements, setTotalElements] = useState();
	const [data, setData] = useState([]);

	const initialFilters = {
		searchTerm: '',
		dateFill: '',
		stateFill: '',
		pageSize: 10,
		pageNo: 1,
	};

	const [filter, setFilter] = useState(initialFilters);
	const getData = useCallback(() => {
		setLoading(true);
		getAssignmentOfStaff(filter)
			.then((res) => {
				const listAsssignmentId = res.data.assignmentIdReq;
				setList(listAsssignmentId);
				const convertedData = res.data.content.map((data) => {
					const {
						assignmentId,
						assetId,
						assignee,
						assigner,
						assignDate,
						state,
					} = data;

					return {
						key: assignmentId,
						assignmentId,
						assetCode: `${assetId.assetId}`,
						assetName: `${assetId.name}`,
						assigner: `${assigner.username}`,
						assignee: `${assignee.username}`,
						assignDate,
						category: `${assetId.category.name}`,
						state: titleCase(state.replaceAll('_', ' ')),
					};
				});
				setDataSource(convertedData);

				setTotalElements(res.data.totalElements);
			})
			.catch((error) => {
				message.error(error.message);
			});

		setLoading(false);
	}, [filter]);
	useEffect(() => {
		getData();
	}, [getData]);

	function titleCase(str) {
		var splitStr = str.toLowerCase().split(' ');
		for (var i = 0; i < splitStr.length; i++) {
			// You do not need to check if i is larger than splitStr length, as your for does that for you
			// Assign it back to the array
			splitStr[i] = splitStr[i].charAt(0).toUpperCase() + splitStr[i].substring(1);
		}
		// Directly return the joined string
		return splitStr.join(' ');
	}

	const [isModalOpen, setIsModalOpen] = useState(false);
	const showModal = () => {
		setIsModalOpen(true);
	};

	const handleCancel = () => {
		setIsModalOpen(false);
	};
	const [isModalDecline, setIsModalDecline] = useState(false);
	const [isModalAccept, setIsModalAccept] = useState(false);
	const [isModalReq, setIsModalReq] = useState(false);
	const showModalAccept = (id) => {
		setidAccept(id);
		setIsModalAccept(true);
	};
	const [idReq, setidReq] = useState('');
	const [idDecline, setidDecline] = useState('');
	const [idAccept, setidAccept] = useState('');
	const handleOkAccept = () => {
		acceptAssignment(idAccept)
			.then(() => {
				message.success("Assignment is accepted successfully")
				getData();
			})
			.catch((err) => message.error(err));
		setIsModalAccept(false);
	};

	const handleCancelAccept = () => {
		setIsModalAccept(false);
	};

	const showModalDecline = (id) => {
		setidDecline(id);
		setIsModalDecline(true);
	};

	const handleOkDecline = () => {
		declineAssignment(idDecline)
			.then(() => {
				message.success("Assignment is declined successfully")
				getData();
			})
			.catch((err) => message.error(err));
		setIsModalDecline(false);
	};

	const handleCancelDecline = () => {
		setIsModalDecline(false);
	};

	const showModalReq = (id) => {
		setidReq(id);
		setIsModalReq(true);
	};
	const {
		authContext: { role },

	} = useContext(AuthContext);
	const handleOkReq = () => {
		if (role === "ADMIN") {
			postReturnReqByAdmin(idReq)
				.then(() => {
					message.success("Create a returning request succesfully")
					getData();
				})
				.catch((err) => message.error(err));
			setIsModalReq(false);
		} else {
			postReturnReq(idReq)
				.then(() => {
					message.success("Create a returning request succesfully")
					getData();
				})
				.catch((err) => message.error(err));
			setIsModalReq(false);
		}



	};

	const handleCancelReq = () => {
		setIsModalReq(false);
	};
	const onPageChange = (pageNo, pageSize) => {
		const newFilter = {
			...filter,
			pageNo,
			pageSize,
		};
		setFilter(newFilter);
	};

	const columns = [
		{
			title: 'Asset code',
			dataIndex: 'assetCode',
			key: dataSource.assetCode,
			sorter: (a, b) => a.assetCode.localeCompare(b.assetCode),

			onCell: (record) => {
				if (record.state === 'Accepted') {
					return {
						onClick: () => {
							handleShowDetailModal(record);
						},
					};
				}
			},
		},
		{
			title: 'Asset Name',
			dataIndex: 'assetName',
			key: dataSource.assetName,
			sorter: (a, b) => a.assetName.localeCompare(b.assetName),
			onCell: (record) => {
				if (record.state === 'Accepted') {
					return {
						onClick: () => {
							handleShowDetailModal(record);
						},
					};
				}
			},
		},

		{
			title: 'Category',
			dataIndex: 'category',
			key: dataSource.category,
			sorter: (a, b) => a.category.localeCompare(b.category),
			onCell: (record) => {
				if (record.state === 'Accepted') {
					return {
						onClick: () => {
							handleShowDetailModal(record);
						},
					};
				}
			},
		},
		{
			title: 'State',
			dataIndex: 'state',
			key: dataSource.state,
			sorter: (a, b) => a.state.localeCompare(b.state),
			onCell: (record) => {
				if (record.state === 'Accepted') {
					return {
						onClick: () => {
							handleShowDetailModal(record);
						},
					};
				}
			},
		},

		{
			title: '',
			key: '',
			render: (_, record) => (
				<span>
					<Button
						disabled={record.state === 'Accepted'}
						className='tick-icon'
						onClick={() => showModalAccept(`${record.assignmentId}`)}
						style={{ borderColor: 'white', padding: '5px 7px' }}>
						<CheckOutlined style={{ color: 'red' }} />
					</Button>

					<Divider type='vertical' />

					<Button
						disabled={record.state === 'Accepted'}
						className='x-icon'
						onClick={() => showModalDecline(`${record.assignmentId}`)}
						style={{ borderColor: 'white', padding: '7px 7px' }}>
						<CloseCircleOutlined style={{ color: 'black' }} />
					</Button>

					<Divider type='vertical' />

					<Button
						disabled={
							record.state === 'Waiting For Acceptance' ||
							list.find((e) => e === record.assignmentId)
						}
						className='return-icon'
						style={{ borderColor: 'white', padding: '7px 7px' }}
						onClick={() => showModalReq(`${record.assignmentId}`)}>
						{' '}
						<ReloadOutlined
							style={{ color: 'blue', position: 'relative', right: '5px' }}
						/>
					</Button>
				</span>
			),
		},
	];
	const detailColumns = [
		{
			title: 'Label',
			dataIndex: 'label',
			key: 'label',
		},
		{
			title: 'Details',
			dataIndex: 'details',
			key: 'details',
		},
	];
	const handleShowDetailModal = (record) => {
		getStaffAssignmentDetail(record.assignmentId)
			.then((res) => {
				const { assigner,
					assignee,
					assetId,
					assignDate,
					assignNote,
					state, } =
					res.data;

				setData([
					{
						label: 'Asset ID',
						details: `${assetId.assetId}`,
					},
					{
						label: 'Asset Name',
						details: `${assetId.name}`,
					},
					{
						label: 'Assigner',
						details: `${assigner.username}`,
					},
					{
						label: 'Assignee',
						details: `${assignee.username}`,
					},
					{
						label: 'Specification',
						details: `${assetId.specification}`,
					},
					{
						label: 'Assign Date',
						details: assignDate,
					},
					{
						label: 'Assign Note',
						details: assignNote,
					},
					{
						label: 'State',
						details: titleCase(state.replaceAll('_', ' ')),
					},
				]);
			})
			.then(showModal);
	};
	return (
		<>
			<h1 className='asset-title'>My Assignments</h1>

			<Table
				columns={columns}
				dataSource={dataSource}
				loading={loading}
				pagination={{
					current: filter.pageNo,
					pageSize: filter.pageSize,
					total: totalElements,
					onChange: onPageChange,
					hideOnSinglePage: true,
				}}
				showSorterTooltip={false}
			/>
			<Modal
				title='Are you sure?'
				open={isModalAccept}
				onOk={handleOkAccept}
				onCancel={handleCancelAccept}
				footer={null}>
				<p>Do you want accept this assignment?</p>
				<Button
					className='button-modal'
					type='danger'
					key='back'
					onClick={handleOkAccept}>
					Accept
				</Button>

				<Button
					className='button-modal'
					key='submit'
					type=''
					loading={loading}
					onClick={handleCancelAccept}>
					Cancel
				</Button>
			</Modal>

			<Modal
				title='Are you sure?'
				open={isModalDecline}
				onOk={handleOkDecline}
				onCancel={handleCancelDecline}
				footer={null}>
				<p>Do you want decline this assignment?</p>
				<Button
					className='button-modal'
					type='danger'
					key='back'
					onClick={handleOkDecline}>
					Decline
				</Button>

				<Button
					className='button-modal'
					key='submit'
					type=''
					loading={loading}
					onClick={handleCancelDecline}>
					Cancel
				</Button>
			</Modal>

			<Modal
				title='Are you sure?'
				open={isModalReq}
				onOk={handleOkReq}
				onCancel={handleCancelReq}
				footer={null}>
				<p>Do you want to create a returning request for this asset?</p>
				<Button
					className='button-modal'
					type='danger'
					key='back'
					onClick={handleOkReq}>
					Yes
				</Button>

				<Button
					className='button-modal'
					key='submit'
					type=''
					loading={loading}
					onClick={handleCancelReq}>
					No
				</Button>
			</Modal>
			<Modal
				title='Detailed Assignment Information'
				open={isModalOpen}
				onCancel={handleCancel}
				footer={null}>
				<Table
					columns={detailColumns}
					dataSource={data}
					size='small'
					pagination={{
						hideOnSinglePage: true,
					}}
				/>
			</Modal>

		</>
	);
};
export default Home;
