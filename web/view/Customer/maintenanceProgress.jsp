<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>AI Tư vấn Bảo dưỡng</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Inter', 'Segoe UI', Roboto, Arial, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
            }

            .app {
                display: flex;
                height: 100vh;
            }

            .container {
                flex: 1;
                margin-left: 260px;
                padding: 40px;
                overflow-y: auto;
            }

            .header {
                background: white;
                padding: 30px;
                border-radius: 15px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.2);
                margin-bottom: 30px;
            }

            .header h1 {
                color: #333;
                font-size: 32px;
                margin-bottom: 10px;
                display: flex;
                align-items: center;
                gap: 12px;
            }

            .header h1 i {
                color: #667eea;
            }

            .ai-section {
                display: grid;
                grid-template-columns: 1fr;
                gap: 25px;
                margin-bottom: 30px;
            }

            /* Chatbot Styles - Improved */
            .chat-container {
                background: white;
                border-radius: 15px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.1);
                height: 650px;
                display: flex;
                flex-direction: column;
                overflow: hidden;
            }

            .chat-header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 20px 25px;
                display: flex;
                align-items: center;
                justify-content: space-between;
                flex-shrink: 0;
            }

            .chat-header-left {
                display: flex;
                align-items: center;
                gap: 12px;
            }

            .chat-header h3 {
                margin: 0;
                font-size: 20px;
                font-weight: 600;
            }

            .chat-header-icon {
                width: 40px;
                height: 40px;
                background: rgba(255, 255, 255, 0.2);
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 18px;
            }

            .chat-car-selector {
                background: white;
                padding: 15px 20px;
                border-bottom: 2px solid #f3f4f6;
                flex-shrink: 0;
            }

            .chat-car-selector select {
                width: 100%;
                padding: 10px 14px;
                border: 2px solid #e5e7eb;
                border-radius: 8px;
                font-size: 14px;
                color: #374151;
                background: white;
                cursor: pointer;
                transition: all 0.3s;
            }

            .chat-car-selector select:focus {
                outline: none;
                border-color: #667eea;
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            }

            .selected-car-info {
                background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
                padding: 12px 20px;
                border-bottom: 1px solid #bae6fd;
                flex-shrink: 0;
                display: none;
            }

            .selected-car-info.show {
                display: block;
            }

            .selected-car-info-content {
                display: flex;
                align-items: center;
                gap: 10px;
                font-size: 13px;
                color: #0369a1;
            }

            .selected-car-info i {
                font-size: 16px;
            }

            .chat-messages {
                flex: 1;
                padding: 20px;
                overflow-y: auto;
                background: #f9fafb;
                display: flex;
                flex-direction: column;
                gap: 12px;
                scroll-behavior: smooth;
            }

            /* Custom scrollbar */
            .chat-messages::-webkit-scrollbar {
                width: 6px;
            }

            .chat-messages::-webkit-scrollbar-track {
                background: #f1f1f1;
            }

            .chat-messages::-webkit-scrollbar-thumb {
                background: #cbd5e1;
                border-radius: 3px;
            }

            .chat-messages::-webkit-scrollbar-thumb:hover {
                background: #94a3b8;
            }

            .message {
                display: flex;
                gap: 10px;
                animation: messageSlideIn 0.3s ease;
                align-items: flex-start;
            }

            @keyframes messageSlideIn {
                from {
                    opacity: 0;
                    transform: translateY(10px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            .message.user {
                justify-content: flex-end;
            }

            .message-avatar {
                width: 32px;
                height: 32px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 14px;
                flex-shrink: 0;
                margin-top: 4px;
            }

            .message.ai .message-avatar {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
            }

            .message.user .message-avatar {
                background: #10b981;
                color: white;
            }

            .message-bubble {
                max-width: 75%;
                padding: 12px 16px;
                border-radius: 16px;
                line-height: 1.6;
                font-size: 14px;
                word-wrap: break-word;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
            }

            .message.user .message-bubble {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border-radius: 16px 16px 4px 16px;
            }

            .message.ai .message-bubble {
                background: white;
                color: #374151;
                border: 1px solid #e5e7eb;
                border-radius: 16px 16px 16px 4px;
            }

            .typing-indicator {
                display: flex;
                gap: 4px;
                padding: 8px 0;
            }

            .typing-indicator span {
                width: 8px;
                height: 8px;
                border-radius: 50%;
                background: #cbd5e1;
                animation: typingBounce 1.4s infinite;
            }

            .typing-indicator span:nth-child(2) {
                animation-delay: 0.2s;
            }

            .typing-indicator span:nth-child(3) {
                animation-delay: 0.4s;
            }

            @keyframes typingBounce {
                0%, 60%, 100% {
                    transform: translateY(0);
                }
                30% {
                    transform: translateY(-8px);
                }
            }

            .chat-input-area {
                padding: 15px 20px 20px;
                background: white;
                border-top: 2px solid #f3f4f6;
                flex-shrink: 0;
            }

            .quick-actions {
                display: flex;
                gap: 8px;
                flex-wrap: wrap;
                margin-bottom: 12px;
            }

            .quick-btn {
                padding: 8px 14px;
                background: #f3f4f6;
                border: 1px solid #e5e7eb;
                border-radius: 20px;
                font-size: 13px;
                cursor: pointer;
                transition: all 0.3s;
                color: #4b5563;
                font-weight: 500;
            }

            .quick-btn:hover {
                background: #667eea;
                color: white;
                border-color: #667eea;
                transform: translateY(-1px);
            }

            .chat-input-form {
                display: flex;
                gap: 10px;
                align-items: center;
            }

            .chat-input {
                flex: 1;
                padding: 12px 16px;
                border: 2px solid #e5e7eb;
                border-radius: 24px;
                font-size: 14px;
                transition: all 0.3s;
                background: #f9fafb;
            }

            .chat-input:focus {
                outline: none;
                border-color: #667eea;
                background: white;
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            }

            .btn-send {
                width: 44px;
                height: 44px;
                padding: 0;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border: none;
                border-radius: 50%;
                font-weight: 700;
                cursor: pointer;
                transition: all 0.3s;
                display: flex;
                align-items: center;
                justify-content: center;
                flex-shrink: 0;
            }

            .btn-send:hover:not(:disabled) {
                transform: scale(1.1);
                box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
            }

            .btn-send:disabled {
                opacity: 0.5;
                cursor: not-allowed;
            }

            @media (max-width: 768px) {
                .container {
                    margin-left: 0;
                    padding: 20px;
                }
                
                .chat-container {
                    height: 550px;
                }

                .message-bubble {
                    max-width: 85%;
                }
            }
        </style>
    </head>
    <body>
        <div class="app">
            <!-- Sidebar -->
            <jsp:include page="/view/layout/sidebar.jsp"/>

            <div class="container">
                <div class="header">
                    <h1><i class="fas fa-robot"></i> AI Tư vấn Bảo dưỡng</h1>
                    <p style="color: #666; margin-top: 10px;">
                        Hệ thống AI thông minh giúp bạn theo dõi và lên lịch bảo dưỡng xe
                    </p>
                </div>

                <div class="ai-section">
                    <!-- Chatbot Card -->
                    <div class="chat-container">
                        <div class="chat-header">
                            <div class="chat-header-left">
                                <div class="chat-header-icon">
                                    <i class="fas fa-robot"></i>
                                </div>
                                <div>
                                    <h3>AI Tư vấn Bảo dưỡng</h3>
                                    <div style="font-size: 12px; opacity: 0.9;">Luôn sẵn sàng hỗ trợ</div>
                                </div>
                            </div>
                        </div>

                        <div class="chat-car-selector">
                            <select id="chatCarSelect" class="form-select" onchange="handleCarSelection()">
                                <option value="">🚗 Chọn xe để nhận tư vấn...</option>
                                <c:forEach items="${carList}" var="car">
                                    <option value="${car.carId}" 
                                            data-brand="${car.brand}" 
                                            data-model="${car.model}" 
                                            data-plate="${car.licensePlate}"
                                            data-odometer="${car.currentOdometer != null ? car.currentOdometer : 0}"
                                            data-last-maintenance="${car.lastMaintenanceDate != null ? car.lastMaintenanceDate : ''}">
                                        ${car.brand} ${car.model} - ${car.licensePlate} 
                                        (<c:out value="${car.currentOdometer != null ? car.currentOdometer : 0}"/> km)
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="selected-car-info" id="selectedCarInfo">
                            <div class="selected-car-info-content">
                                <i class="fas fa-car"></i>
                                <span id="carInfoText">Chưa chọn xe</span>
                            </div>
                        </div>

                        <div class="chat-messages" id="chatMessages">
                            <div class="message ai">
                                <div class="message-avatar">
                                    <i class="fas fa-robot"></i>
                                </div>
                                <div style="display: flex; flex-direction: column; max-width: 75%;">
                                    <div class="message-bubble">
                                        👋 Xin chào! Tôi là trợ lý AI chuyên về bảo dưỡng ô tô. Hãy chọn xe của bạn ở trên để tôi có thể tư vấn chính xác dựa trên thông tin xe nhé!
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="chat-input-area">
                            <div class="quick-actions">
                                <button class="quick-btn" onclick="sendQuickMessage('Xe tôi cần bảo dưỡng gì?')">
                                    🔧 Cần bảo dưỡng gì?
                                </button>
                                <button class="quick-btn" onclick="sendQuickMessage('Bao lâu nên thay dầu?')">
                                    🛢️ Thay dầu khi nào?
                                </button>
                                <button class="quick-btn" onclick="sendQuickMessage('Giá bảo dưỡng khoảng bao nhiêu?')">
                                    💰 Giá bảo dưỡng?
                                </button>
                            </div>

                            <form class="chat-input-form" id="chatForm" onsubmit="sendMessage(event)">
                                <input type="text" class="chat-input" id="chatInput" 
                                       placeholder="Nhập câu hỏi của bạn..." required autocomplete="off">
                                <button type="submit" class="btn-send" id="sendBtn">
                                    <i class="fas fa-paper-plane"></i>
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            let isTyping = false;
            let selectedCarData = null;

            // Xử lý khi chọn xe
            function handleCarSelection() {
                const select = document.getElementById('chatCarSelect');
                const selectedOption = select.options[select.selectedIndex];
                const carInfoDiv = document.getElementById('selectedCarInfo');
                const carInfoText = document.getElementById('carInfoText');

                if (select.value) {
                    // Lưu thông tin xe đã chọn
                    selectedCarData = {
                        carId: select.value,
                        brand: selectedOption.dataset.brand,
                        model: selectedOption.dataset.model,
                        licensePlate: selectedOption.dataset.plate,
                        currentOdometer: selectedOption.dataset.odometer,
                        lastMaintenanceDate: selectedOption.dataset.lastMaintenance
                    };

                    // Hiển thị thông tin xe đã chọn
                    carInfoText.textContent = `${selectedCarData.brand} ${selectedCarData.model} - ${selectedCarData.licensePlate} (${selectedCarData.currentOdometer} km)`;
                    carInfoDiv.classList.add('show');

                    // Thông báo AI về xe đã chọn
                    addMessage(`Tôi đã chọn xe: ${selectedCarData.brand} ${selectedCarData.model} (${selectedCarData.licensePlate})`, 'user');
                    
                    const contextMessage = selectedCarData.lastMaintenanceDate 
                        ? `Xe có số km hiện tại: ${selectedCarData.currentOdometer} km, lần bảo dưỡng gần nhất: ${selectedCarData.lastMaintenanceDate}`
                        : `Xe có số km hiện tại: ${selectedCarData.currentOdometer} km, chưa có lịch sử bảo dưỡng`;
                    
                    addMessage(`✅ Đã ghi nhận! ${contextMessage}. Bạn muốn hỏi gì về xe này?`, 'ai');
                } else {
                    selectedCarData = null;
                    carInfoDiv.classList.remove('show');
                }
            }

            function sendMessage(event) {
                event.preventDefault();
                
                if (isTyping) return;
                
                const input = document.getElementById('chatInput');
                const message = input.value.trim();

                if (!selectedCarData) {
                    showAlert("Vui lòng chọn xe trước khi chat với AI!");
                    return;
                }

                if (message) {
                    addMessage(message, 'user');
                    input.value = '';
                    
                    setInputState(false);
                    callAI(message);
                }
            }

            function sendQuickMessage(message) {
                if (!selectedCarData) {
                    showAlert("Vui lòng chọn xe trước khi chat với AI!");
                    return;
                }

                if (isTyping) return;

                addMessage(message, 'user');
                setInputState(false);
                callAI(message);
            }

            function addMessage(text, sender) {
                const messagesDiv = document.getElementById('chatMessages');
                const messageDiv = document.createElement('div');
                messageDiv.className = 'message ' + sender;

                const avatar = document.createElement('div');
                avatar.className = 'message-avatar';
                avatar.innerHTML = sender === 'user' ? '<i class="fas fa-user"></i>' : '<i class="fas fa-robot"></i>';

                const contentWrapper = document.createElement('div');
                contentWrapper.style.display = 'flex';
                contentWrapper.style.flexDirection = 'column';
                contentWrapper.style.maxWidth = '75%';

                const bubble = document.createElement('div');
                bubble.className = 'message-bubble';
                bubble.textContent = text;

                contentWrapper.appendChild(bubble);
                
                if (sender === 'user') {
                    messageDiv.appendChild(contentWrapper);
                    messageDiv.appendChild(avatar);
                } else {
                    messageDiv.appendChild(avatar);
                    messageDiv.appendChild(contentWrapper);
                }

                messagesDiv.appendChild(messageDiv);
                scrollToBottom();
            }

            function showTypingIndicator() {
                const messagesDiv = document.getElementById('chatMessages');
                const typingDiv = document.createElement('div');
                typingDiv.id = 'typingIndicator';
                typingDiv.className = 'message ai';
                
                typingDiv.innerHTML = `
                    <div class="message-avatar">
                        <i class="fas fa-robot"></i>
                    </div>
                    <div style="display: flex; flex-direction: column; max-width: 75%;">
                        <div class="message-bubble">
                            <div class="typing-indicator">
                                <span></span>
                                <span></span>
                                <span></span>
                            </div>
                        </div>
                    </div>
                `;
                
                messagesDiv.appendChild(typingDiv);
                scrollToBottom();
            }

            function removeTypingIndicator() {
                const typingIndicator = document.getElementById('typingIndicator');
                if (typingIndicator) {
                    typingIndicator.remove();
                }
            }

            function callAI(message) {
                isTyping = true;
                showTypingIndicator();

                // Tạo context đầy đủ về xe
                const carContext = {
                    carId: selectedCarData.carId,
                    brand: selectedCarData.brand,
                    model: selectedCarData.model,
                    licensePlate: selectedCarData.licensePlate,
                    currentOdometer: selectedCarData.currentOdometer,
                    lastMaintenanceDate: selectedCarData.lastMaintenanceDate || 'Chưa có'
                };

                fetch('${pageContext.request.contextPath}/freeAiReminder', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    body: 'action=chat&message=' + encodeURIComponent(message) + 
                          '&carId=' + encodeURIComponent(carContext.carId) +
                          '&brand=' + encodeURIComponent(carContext.brand) +
                          '&model=' + encodeURIComponent(carContext.model) +
                          '&odometer=' + encodeURIComponent(carContext.currentOdometer) +
                          '&lastMaintenance=' + encodeURIComponent(carContext.lastMaintenanceDate)
                })
                .then(res => res.json())
                .then(data => {
                    removeTypingIndicator();
                    isTyping = false;
                    setInputState(true);
                    
                    if (data.error) {
                        addMessage('❌ Lỗi: ' + data.error, 'ai');
                    } else {
                        const aiResponse = data.response || 'Xin lỗi, tôi không thể trả lời câu hỏi này.';
                        addMessage(aiResponse, 'ai');
                    }
                })
                .catch(err => {
                    removeTypingIndicator();
                    isTyping = false;
                    setInputState(true);
                    addMessage('❌ Không thể kết nối với AI. Vui lòng thử lại sau.', 'ai');
                    console.error('AI Error:', err);
                });
            }

            function setInputState(enabled) {
                const input = document.getElementById('chatInput');
                const sendBtn = document.getElementById('sendBtn');
                
                input.disabled = !enabled;
                sendBtn.disabled = !enabled;
                
                if (enabled) {
                    input.focus();
                }
            }

            function scrollToBottom() {
                const messagesDiv = document.getElementById('chatMessages');
                setTimeout(() => {
                    messagesDiv.scrollTop = messagesDiv.scrollHeight;
                }, 100);
            }

            function showAlert(message) {
                const alertDiv = document.createElement('div');
                alertDiv.style.cssText = `
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
                    color: white;
                    padding: 16px 24px;
                    border-radius: 12px;
                    box-shadow: 0 10px 25px rgba(239, 68, 68, 0.3);
                    z-index: 10000;
                    animation: slideInRight 0.3s ease;
                `;
                alertDiv.innerHTML = `
                    <i class="fas fa-exclamation-circle"></i> ${message}
                `;
                
                document.body.appendChild(alertDiv);
                
                setTimeout(() => {
                    alertDiv.style.animation = 'slideOutRight 0.3s ease';
                    setTimeout(() => alertDiv.remove(), 300);
                }, 3000);
            }

            const style = document.createElement('style');
            style.textContent = `
                @keyframes slideInRight {
                    from {
                        transform: translateX(100%);
                        opacity: 0;
                    }
                    to {
                        transform: translateX(0);
                        opacity: 1;
                    }
                }
                @keyframes slideOutRight {
                    from {
                        transform: translateX(0);
                        opacity: 1;
                    }
                    to {
                        transform: translateX(100%);
                        opacity: 0;
                    }
                }
            `;
            document.head.appendChild(style);

            window.addEventListener('load', function() {
                document.getElementById('chatInput').focus();
            });
        </script>
    </body>
</html>